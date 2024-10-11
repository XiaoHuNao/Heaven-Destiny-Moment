package com.xiaohunao.heaven_destiny_moment.common.moment;

import com.google.common.collect.Sets;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.client.gui.bar.MomentBar;
import com.xiaohunao.heaven_destiny_moment.common.capability.MomentCap;
import com.xiaohunao.heaven_destiny_moment.common.context.EntityBattlePointContext;
import com.xiaohunao.heaven_destiny_moment.common.event.MomentEvent;
import com.xiaohunao.heaven_destiny_moment.common.init.ModMoments;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class MomentInstance {
    private static final EntityBattlePointContext entityBattlePointContext = EntityBattlePointContext.CONTEXT;

    private final ResourceKey<Moment> momentKey;
    private final Moment moment;
    private final MomentBar bar;
    private final Level level;
    private final UUID uuid;


    protected long tick;
    private int battlePoint = 0;
    protected MomentState state = MomentState.READY;
    protected Set<UUID> players = Sets.newHashSet();
    private MomentInstance(ResourceKey<Moment> momentKey,Level level) {
        this.momentKey = momentKey;
        this.moment = getMoment(momentKey,level);
        this.level = level;
        this.uuid = UUID.randomUUID();
        this.bar = new MomentBar(this.uuid, momentKey);
    }
    private MomentInstance(ResourceKey<Moment> momentKey,Level level,UUID uuid) {
        this.momentKey = momentKey;
        this.moment = getMoment(momentKey,level);
        this.level = level;
        this.uuid = uuid;
        this.bar = new MomentBar(uuid, momentKey);
    }

    public static MomentInstance create(ResourceKey<Moment> momentKey,Level level,BlockPos pos, @Nullable Player player) {
        MomentInstance momentInstance = create(momentKey,level);
        if (momentInstance != null) {
            if (momentInstance.canCreate(pos, player)) {
                MomentCap.getCap(level).ifPresent(cap -> cap.addMoment(momentInstance));
                return momentInstance;
            }
        }
        return null;
    }
    public static Moment getMoment(ResourceKey<Moment> momentKey,Level level) {
        Registry<Moment> registryChecked = getRegistryChecked(momentKey, level);
        if (registryChecked == null) {
            return null;
        }
        return registryChecked.get(momentKey);
    }
    public static MomentInstance create(ResourceKey<Moment> momentKey,Level level) {
        Registry<Moment> registryChecked = getRegistryChecked(momentKey, level);
        if (registryChecked == null) {
            return null;
        }
        return new MomentInstance(momentKey,level);
    }
    public static MomentInstance create(ResourceKey<Moment> momentKey,Level level,UUID uuid) {
        Registry<Moment> registryChecked = getRegistryChecked(momentKey, level);
        if (registryChecked == null) {
            return null;
        }
        return new MomentInstance(momentKey,level,uuid);
    }
    private static Registry<Moment> getRegistryChecked(ResourceKey<Moment> momentKey, Level level) {
        Registry<Moment> registry = level.registryAccess().registryOrThrow(ModMoments.MOMENT_KEY);
        if (registry.getHolder(momentKey).isEmpty()) {
            HeavenDestinyMoment.LOGGER.error("Moment {} not found in registry", momentKey.location());
            return null;
        }
        return registry;
    }

    public boolean canCreate(BlockPos pos, @Nullable Player player) {
        return getMoment().momentDataContext().canCreate(this,level,pos,player);
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
        bar.getPlayers().forEach(player -> this.players.add(player.getUUID()));
    }

    public void setState(MomentState state) {
        MinecraftForge.EVENT_BUS.post(MomentEvent.create(this, state));
        this.state = state;
    }


    public Moment getMoment() {
        return moment;
    }

    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        ResourceKey.codec(ModMoments.MOMENT_KEY).encodeStart(NbtOps.INSTANCE, momentKey).resultOrPartial(HeavenDestinyMoment.LOGGER::error).ifPresent(tag -> compoundTag.put("moment_key", tag));
        compoundTag.putUUID("uuid", uuid);

        compoundTag.putLong("tick", tick);
        compoundTag.putInt("battle_point", battlePoint);
        compoundTag.putString("state", state.name());
        ListTag tags = new ListTag();
        players.forEach(player -> tags.add(StringTag.valueOf(player.toString())));
        compoundTag.put("players", tags);
        return compoundTag;
    }
    public void deserializeNBT(CompoundTag compoundTag) {
        tick = compoundTag.getLong("tick");
        battlePoint = compoundTag.getInt("battle_point");
        state = MomentState.valueOf(compoundTag.getString("state"));
        ListTag tags = compoundTag.getList("players", Tag.TAG_STRING);
        tags.forEach(tag -> players.add(UUID.fromString(tag.getAsString())));
    }



    public final void baseTick() {
        tick++;
        if (level != null) {
            updatePlayers();
            tick();
        }
    }
    public void tick() {

    }

    public static MomentInstance createFromCompoundTag(CompoundTag compoundTag,Level level) {
        Optional<ResourceKey<Moment>> result = ResourceKey.codec(ModMoments.MOMENT_KEY).parse(NbtOps.INSTANCE, compoundTag.get("moment_key")).result();
        if (result.isEmpty()) {
            HeavenDestinyMoment.LOGGER.error("Moment key not found in compound tag");
            return null;
        }
        UUID uuid = compoundTag.getUUID("uuid");
        MomentInstance momentInstance = MomentInstance.create(result.get(),level,uuid);
        if (momentInstance == null) {
            return null;
        }
        momentInstance.deserializeNBT(compoundTag);
        momentInstance.bar.setID(uuid);
        return momentInstance;
    }

    public UUID getID() {
        return uuid;
    }

    public Level getLevel() {
        return level;
    }

    public Double getSpawnMultiplier(MobCategory mobCategory) {
        return moment.momentDataContext().getMobSpawnSettingsContext().getSpawnMultiplier(mobCategory);
    }
}
