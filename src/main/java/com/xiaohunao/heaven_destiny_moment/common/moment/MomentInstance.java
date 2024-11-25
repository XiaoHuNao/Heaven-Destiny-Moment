package com.xiaohunao.heaven_destiny_moment.common.moment;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.client.gui.bar.MomentBar;
import com.xiaohunao.heaven_destiny_moment.common.attachment.MomentKillEntityAttachment;
import com.xiaohunao.heaven_destiny_moment.common.event.MomentEvent;
import com.xiaohunao.heaven_destiny_moment.common.event.PlayerMomentAreaEvent;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMAttachments;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMRegistries;
import net.minecraft.core.Registry;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

public abstract class MomentInstance extends AttachmentHolder {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final Level level;
    private final MomentType<?> type;
    private final ResourceKey<Moment> momentKey;
    private final Moment moment;
    private final UUID uuid;
    private final MomentBar bar;

    private long tick = -1L;
    private MomentState state;
    private final Set<UUID> playerUUIDs = Sets.newHashSet();
    private final Set<Player> players = Sets.newHashSet();
    private final Set<UUID> inAreaPlayers = Sets.newHashSet();

    protected MomentInstance(MomentType<?> type, Level level, ResourceKey<Moment> momentKey) {
        this.uuid = UUID.randomUUID();
        this.type = type;
        this.level = level;
        this.momentKey = momentKey;
        this.moment = Objects.requireNonNull(registryChecked(momentKey, level)).get(momentKey);
        this.bar = new MomentBar(uuid, moment.getBarRenderType());
    }

    protected MomentInstance(MomentType<?> type, UUID uuid, Level level, ResourceKey<Moment> momentKey) {
        this.uuid = uuid;
        this.type = type;
        this.level = level;
        this.momentKey = momentKey;
        this.moment = Objects.requireNonNull(registryChecked(momentKey, level)).get(momentKey);
        this.bar = new MomentBar(uuid, moment.getBarRenderType());
    }

    public static boolean create(ResourceKey<Moment> momentKey, ServerLevel level) {
        Moment moment = registryChecked(momentKey, level).get(momentKey);
        if (moment != null) {
            MomentInstance momentInstance = moment.newMomentInstance(level, momentKey);
            MomentManager.of(level).addMoment(level, momentInstance);
            return true;
        }
        return false;
    }

    public static Registry<Moment> registryChecked(ResourceKey<Moment> momentKey, Level level) {
        Registry<Moment> registry = level.registryAccess().registryOrThrow(HDMRegistries.Keys.MOMENT);
        if (registry.getHolder(momentKey).isEmpty()) {
            HeavenDestinyMoment.LOGGER.error("Moment {} not found in registry", momentKey.location());
            return null;
        }
        return registry;
    }

    @Nullable
    public static MomentInstance loadStatic(Level level, CompoundTag compoundTag) {
        String id = compoundTag.getString("id");
        ResourceLocation resourcelocation = ResourceLocation.tryParse(id);
        if (resourcelocation == null) {
            LOGGER.error("MomentInstance has invalid type: {}", id);
            return null;
        } else {
            return HDMRegistries.MOMENT_TYPE.getOptional(resourcelocation).map(momentType -> {
                try {
                    Tag tag = compoundTag.get("moment");
                    return momentType.create(compoundTag.getUUID("uuid"), level, ResourceKey.codec(HDMRegistries.Keys.MOMENT).decode(NbtOps.INSTANCE, tag).getOrThrow().getFirst());
                } catch (Throwable throwable) {
                    LOGGER.error("Failed to create MomentInstance {}", id, throwable);
                    return null;
                }
            }).map(momentType -> {
                try {
                    momentType.deserializeNBT(compoundTag);
                    return momentType;
                } catch (Throwable throwable) {
                    LOGGER.error("Failed to load data for MomentInstance {}", id, throwable);
                    return null;
                }
            }).orElseGet(() -> {
                LOGGER.warn("Skipping MomentInstance with id {}", id);
                return null;
            });
        }
    }

    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();

        serializeMetadata(compoundTag);
        compoundTag.putLong("tick", tick);
        if (state != null) {
            compoundTag.putString("state", state.name());
        }

        ListTag tags = new ListTag();
        playerUUIDs.forEach(uuid -> tags.add(StringTag.valueOf(uuid.toString())));
        compoundTag.put("player_uuids", tags);

        return compoundTag;
    }

    public void deserializeNBT(CompoundTag compoundTag) {
        this.tick = compoundTag.getLong("tick");
        if (compoundTag.contains("state")) {
            this.state = MomentState.valueOf(compoundTag.getString("state"));
        }

        ListTag tags = compoundTag.getList("player_uuids", Tag.TAG_STRING);
        tags.forEach(tag -> playerUUIDs.add(UUID.fromString(tag.getAsString())));
    }

    private void serializeMetadata(CompoundTag compoundTag) {
        compoundTag.putUUID("uuid", uuid);
        compoundTag.putString("id", MomentInstance.getRegistryName(type).toString());
        compoundTag.put("moment", ResourceKey.codec(HDMRegistries.Keys.MOMENT).encodeStart(NbtOps.INSTANCE, momentKey).getOrThrow());
    }

    public static ResourceLocation getRegistryName(MomentType<?> momentType) {
        return HDMRegistries.MOMENT_TYPE.getKey(momentType);
    }

    public Level getLevel() {
        return level;
    }

    public UUID getID() {
        return uuid;
    }

    public boolean shouldEnd() {
        return state == MomentState.END;
    }

    public final void baseTick() {
        this.tick++;

        updatePlayers();
        updatePlayerIsInArea();

        if (tick == 0L) {
            setState(MomentState.READY);
        }

        if (!players.isEmpty()) {
            NeoForge.EVENT_BUS.post(new MomentEvent.Tick(this));
            tick();
        }
    }

    public void tick() {

    }

    public void setState(MomentState state) {
        this.state = state;
        NeoForge.EVENT_BUS.post(MomentEvent.getEventToPost(this, state));
        moment.getTipSettingsContext().playTooltip(this);
    }


    public Predicate<Player> validPlayer() {
        return player -> !player.isSpectator();
    }

    public List<Player> getPlayers(Predicate<? super Player> predicate) {
        List<Player> list = Lists.newArrayList();

        for(Player player : level.players()) {
            if (predicate.test(player)) {
                list.add(player);
            }
        }
        return list;
    }

    private void updatePlayers() {

        final Set<Player> oldPlayers = Sets.newHashSet(bar.getPlayers());
        final Set<Player> newPlayers = Sets.newHashSet((getPlayers(validPlayer())));
        players.clear();

        newPlayers.stream()
                .filter(player -> !oldPlayers.contains(player))
                .forEach(serverPlayer -> {
                    if (MomentManager.of(level).addPlayerToMoment(serverPlayer,this)) {
                        bar.addPlayer(serverPlayer);
                    }
                });
        oldPlayers.stream()
                .filter(player -> !newPlayers.contains(player))
                .forEach(bar::removePlayer);
        bar.getPlayers().forEach(player -> {
            playerUUIDs.add(player.getUUID());
            players.add(player);
        });
    }

    private void updatePlayerIsInArea() {
        if (level.isClientSide) return;

        players.stream().filter(Objects::nonNull).forEach(player -> {
            boolean inArea = moment.isInArea((ServerLevel) level, player.blockPosition());
            boolean uuidContains = inAreaPlayers.contains(player.getUUID());
            if (inArea && !uuidContains) {
                onPlayerEnterArea((ServerPlayer) player);
            } else if (!inArea && uuidContains) {
                onPlayerExitArea((ServerPlayer) player);
            }
        });
    }

    private void onPlayerExitArea(ServerPlayer player) {
        inAreaPlayers.remove(player.getUUID());
        NeoForge.EVENT_BUS.post(new PlayerMomentAreaEvent.Exit(player, moment.getArea()));
    }

    private void onPlayerEnterArea(ServerPlayer player) {
        inAreaPlayers.add(player.getUUID());
        NeoForge.EVENT_BUS.post(new PlayerMomentAreaEvent.Enter(player, moment.getArea()));
    }

    public Optional<Moment> getMoment() {
        return Optional.ofNullable(moment);
    }

    public MomentState getState() {
        return state;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public MomentBar getBar() {
        return bar;
    }

    public abstract void finalizeSpawn(Entity entity);

    public void addKillCount(LivingEntity livingEntity) {
        this.setData(HDMAttachments.MOMENT_KILL_ENTITY,getData(HDMAttachments.MOMENT_KILL_ENTITY).addKillCount(livingEntity));
    }
}
