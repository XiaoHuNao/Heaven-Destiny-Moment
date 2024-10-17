package com.xiaohunao.heaven_destiny_moment.common.moment;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.client.gui.bar.MomentBar;
import com.xiaohunao.heaven_destiny_moment.common.capability.MomentCap;
import com.xiaohunao.heaven_destiny_moment.common.context.ClientSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.context.EntityBattlePointContext;
import com.xiaohunao.heaven_destiny_moment.common.context.TipSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.AmountContext;
import com.xiaohunao.heaven_destiny_moment.common.context.entity_info.EntityInfoContext;
import com.xiaohunao.heaven_destiny_moment.common.event.MomentEvent;
import com.xiaohunao.heaven_destiny_moment.common.init.ModMoments;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

public class MomentInstance {
    private static final EntityBattlePointContext entityBattlePointContext = EntityBattlePointContext.CONTEXT;

    protected final ResourceKey<Moment> momentKey;
    protected final Moment moment;
    protected final MomentBar bar;
    protected final Level level;
    protected final UUID uuid;


    protected long tick = 0;
    protected int battlePoint = 0;
    protected int wave = 0;
    protected int currentEnemyCount = 0;
    protected int killCount = 0;
    protected MomentState state = MomentState.READY;
    protected Set<UUID> players = Sets.newHashSet();
    private Map<UUID,BlockPos> playersPos = Maps.newHashMap();

    private MomentInstance(ResourceKey<Moment> momentKey,Level level) {
        this.momentKey = momentKey;
        this.moment = getMoment(momentKey,level);
        this.level = level;
        this.uuid = UUID.randomUUID();
        this.bar = new MomentBar(this.uuid, momentKey,moment.barRenderType);
    }
    private MomentInstance(ResourceKey<Moment> momentKey,Level level,UUID uuid) {
        this.momentKey = momentKey;
        this.moment = getMoment(momentKey,level);
        this.level = level;
        this.uuid = uuid;
        this.bar = new MomentBar(uuid, momentKey,moment.barRenderType);
    }

    public static MomentInstance create(ResourceKey<Moment> momentKey, Level level, BlockPos pos, @Nullable Player player) {
        MomentInstance momentInstance = create(momentKey,level);
        MomentCap cap = MomentCap.getCap(level);
        if (momentInstance != null) {
            if (momentInstance.canCreate(pos, player) && momentInstance.moment.isCompatible(cap.getRunMoment().values())) {
                cap.addMoment(momentInstance);
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
    public static MomentInstance create(ResourceKey<Moment> momentKey,UUID uuid,Level level) {
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
    public void playTip(){
        TipSettingsContext tipSettingsContext = moment.clientSettingsContext.tipSettingsContext();
        tipSettingsContext.play(this);
    }
    public float remainingEnemyPercent() {
        int sum = 0;
        List<List<EntityInfoContext>> waves = moment.momentDataContext.waves();
        if (!waves.isEmpty()) {
            for (EntityInfoContext entityInfoContext : waves.get(wave)) {
                int spawnAmount = entityInfoContext.getSpawnAmount();
                sum += spawnAmount;
            }
        }
        return (float) currentEnemyCount / sum;
    }
    public int getWaveCount() {
        return moment.momentDataContext.waves().size();
    }

    public void ready(){
        if (state == MomentState.READY) {
            int readTime = moment.momentDataContext().readyTime();
            if (tick == readTime){
                setState(MomentState.START);
                return;
            }
            bar.updateProgress(1 - ((float) tick / readTime));
        }
    }

    public boolean canCreate(BlockPos pos, @Nullable Player player) {
        return getMoment().momentDataContext().canCreate(this,level,pos,player);
    }

    public boolean shouldEnd() {
        return moment.shouldEnd(this);
    }
    public int getWave() {
        return wave;
    }

    public int getCurrentEnemyCount() {
        return currentEnemyCount;
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
            playersPos.put(player.getUUID(),player.blockPosition());
        });


    }

    public Set<ServerPlayer> getPlayers() {
        return bar.getPlayers();
    }
    public Map<UUID,BlockPos> getPlayersPos() {
        return playersPos;
    }

    public void setState(MomentState state) {
        MinecraftForge.EVENT_BUS.post(MomentEvent.create(this, state));
        this.state = state;
    }


    public Moment getMoment() {
        return moment;
    }

    public int getKillCount() {
        return killCount;
    }

    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        ResourceKey.codec(ModMoments.MOMENT_KEY).encodeStart(NbtOps.INSTANCE, momentKey).resultOrPartial(HeavenDestinyMoment.LOGGER::error).ifPresent(tag -> compoundTag.put("moment_key", tag));
        compoundTag.putUUID("uuid", uuid);

        compoundTag.putLong("tick", tick);
        compoundTag.putInt("battle_point", battlePoint);
        compoundTag.putString("state", state.name());
        compoundTag.putInt("wave", wave);
        compoundTag.putInt("kill_count", killCount);
        compoundTag.putInt("current_enemy_count", currentEnemyCount);
        ListTag tags = new ListTag();
        players.forEach(player -> tags.add(StringTag.valueOf(player.toString())));
        compoundTag.put("players", tags);
        return compoundTag;
    }
    public void deserializeNBT(CompoundTag compoundTag) {
        tick = compoundTag.getLong("tick");
        battlePoint = compoundTag.getInt("battle_point");
        state = MomentState.valueOf(compoundTag.getString("state"));
        wave = compoundTag.getInt("wave");
        killCount = compoundTag.getInt("kill_count");
        currentEnemyCount = compoundTag.getInt("current_enemy_count");
        ListTag tags = compoundTag.getList("players", Tag.TAG_STRING);
        tags.forEach(tag -> players.add(UUID.fromString(tag.getAsString())));
    }



    public final void baseTick() {
        tick++;
        if (level != null) {
            MinecraftForge.EVENT_BUS.post(new MomentEvent.Tick(this));
            updatePlayers();
            if (tick == 0){
                setState(MomentState.READY);
            }
            ready();
            tick();
        }
    }
    public void tick() {
        moment.tick(this);
    }

    public static MomentInstance createFromCompoundTag(CompoundTag compoundTag,Level level) {
        Optional<ResourceKey<Moment>> result = ResourceKey.codec(ModMoments.MOMENT_KEY).parse(NbtOps.INSTANCE, compoundTag.get("moment_key")).result();
        if (result.isEmpty()) {
            HeavenDestinyMoment.LOGGER.error("Moment key not found in compound tag");
            return null;
        }
        UUID uuid = compoundTag.getUUID("uuid");
        MomentInstance momentInstance = MomentInstance.create(result.get(),uuid,level);
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
        return moment.momentDataContext().mobSpawnSettingsContext().getSpawnMultiplier(mobCategory);
    }

    public void finalizeSpawn(LivingEntity livingEntity) {
        moment.finalizeSpawn(this,livingEntity);
    }

    public void addKillCount(LivingEntity livingEntity) {
        killCount++;
        //TODO: 待更新Bar
    }

    public int getBattlePoint() {
        return battlePoint;
    }

    public ResourceKey<Moment> getMomentKey() {
        return momentKey;
    }

    public MomentState getState() {
        return state;
    }

    public MomentBar getBar() {
        return bar;
    }
}
