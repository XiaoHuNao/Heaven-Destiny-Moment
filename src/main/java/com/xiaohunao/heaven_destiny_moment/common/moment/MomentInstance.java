package com.xiaohunao.heaven_destiny_moment.common.moment;

import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.client.gui.bar.MomentBar;
import com.xiaohunao.heaven_destiny_moment.common.event.MomentEvent;
import com.xiaohunao.heaven_destiny_moment.common.event.PlayerEnterExitMomentAreaEvent;
import com.xiaohunao.heaven_destiny_moment.common.init.MomentRegistries;
import com.xiaohunao.heaven_destiny_moment.common.moment.area.Area;
import net.minecraft.core.Registry;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public abstract class MomentInstance {
    private static final Logger LOGGER = LogUtils.getLogger();


    private final Level level;
    private final MomentType<?> type;
    private final ResourceKey<Moment> momentKey;
    private final Moment moment;
    private final UUID uuid;
    private final MomentBar bar;

    private long tick;
    private MomentState state;
    private boolean isPlayerInArea = true;
    private final Set<UUID> players = Sets.newHashSet();
    private final Set<UUID> inAreaPlayers = Sets.newHashSet();


    protected MomentInstance(MomentType<?> type, Level level, ResourceKey<Moment> momentKey) {
        this.uuid = UUID.randomUUID();
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
        Registry<Moment> registry = level.registryAccess().registryOrThrow(MomentRegistries.Keys.MOMENT);
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
            return MomentRegistries.MOMENT_TYPE.getOptional(resourcelocation).map((momentType) -> {
                try {
                    Tag tag = compoundTag.get("moment");
                    return momentType.create(level,ResourceKey.codec(MomentRegistries.Keys.MOMENT).decode(NbtOps.INSTANCE, tag).getOrThrow().getFirst());
                } catch (Throwable throwable) {
                    LOGGER.error("Failed to create MomentInstance {}", id, throwable);
                    return null;
                }
            }).map((momentType) -> {
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
        compoundTag.putLong("tick",tick);
        if (state != null) {
            compoundTag.putString("state", state.name());
        }
        compoundTag.putBoolean("isPlayerInArea", isPlayerInArea);

        ListTag tags = new ListTag();
        players.forEach(uuid -> tags.add(StringTag.valueOf(uuid.toString())));
        compoundTag.put("players", tags);

        return compoundTag;

    }
    public void deserializeNBT(CompoundTag compoundTag) {
        this.tick = compoundTag.getLong("tick");
        if (compoundTag.contains("state")) {
            this.state = MomentState.valueOf(compoundTag.getString("state"));
        }
        this.isPlayerInArea = compoundTag.getBoolean("isPlayerInArea");

        ListTag tags = compoundTag.getList("players", Tag.TAG_STRING);
        tags.forEach(tag -> players.add(UUID.fromString(tag.getAsString())));
    }
    private void serializeMetadata(CompoundTag compoundTag) {
        compoundTag.putString("id",MomentInstance.getRegistryName(type).toString());
        compoundTag.put("moment",ResourceKey.codec(MomentRegistries.Keys.MOMENT).encodeStart(NbtOps.INSTANCE, momentKey).getOrThrow());
    }


    public static ResourceLocation getRegistryName(MomentType<?> momentType) {
        return MomentRegistries.MOMENT_TYPE.getKey(momentType);
    }

    public Level getLevel() {
        return this.level;
    }

    public UUID getID() {
        return this.uuid;
    }

    public boolean shouldEnd() {
        return state == MomentState.END;
    }

    public final void baseTick() {
        tick++;
        if (tick == 0){
            setState(MomentState.READY);
        }
        updatePlayers();
        updatePlayerIsInArea();
        if (isPlayerInArea) {
            NeoForge.EVENT_BUS.post(new MomentEvent.Tick(this));
            tick();
        }

    }

    public void tick() {

    }

    public void setState(MomentState state) {
        NeoForge.EVENT_BUS.post(MomentEvent.create(this, state));
        this.state = state;
    }


    public Predicate<ServerPlayer> validPlayer() {
        return player -> !player.isSpectator();
    }

    private void updatePlayers() {
        if (level.isClientSide()) {
            return;
        }

        final Set<ServerPlayer> oldPlayers = Sets.newHashSet(bar.getPlayers());
        final Set<ServerPlayer> newPlayers = Sets.newHashSet(((ServerLevel)level).getPlayers(this.validPlayer()));
        this.players.clear();
        newPlayers.stream()
                .filter(player -> !oldPlayers.contains(player))
                .forEach(bar::addPlayer);
        oldPlayers.stream()
                .filter(player -> !newPlayers.contains(player))
                .forEach(bar::removePlayer);
        bar.getPlayers().forEach(player -> {
            this.players.add(player.getUUID());
        });
    }

    private void updatePlayerIsInArea() {
        if (level.isClientSide()) {
            return;
        }
        players.stream()
                .map(level::getPlayerByUUID)
                .filter(Objects::nonNull)
                .forEach(player -> {
                    boolean inArea = moment.isInArea((ServerLevel) level, player.blockPosition());
                    boolean uuidContains = inAreaPlayers.contains(player.getUUID());
                    if (inArea && !uuidContains) {
                        onPlayerEnterArea((ServerPlayer)player);
                    } else if (!inArea && uuidContains) {
                        onPlayerExitArea((ServerPlayer)player);
                    }
                });
    }

    private void onPlayerExitArea(ServerPlayer player) {
        inAreaPlayers.remove(player.getUUID());
        NeoForge.EVENT_BUS.post(new PlayerEnterExitMomentAreaEvent.Exit(player, moment.getArea()));
    }

    private void onPlayerEnterArea(ServerPlayer player) {
        inAreaPlayers.add(player.getUUID());
        NeoForge.EVENT_BUS.post(new PlayerEnterExitMomentAreaEvent.Enter(player, moment.getArea()));
    }


    public Moment getMoment() {
        return moment;
    }
}
