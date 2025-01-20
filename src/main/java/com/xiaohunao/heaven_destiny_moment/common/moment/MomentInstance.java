package com.xiaohunao.heaven_destiny_moment.common.moment;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.client.gui.bar.MomentBar;
import com.xiaohunao.heaven_destiny_moment.common.context.EntitySpawnSettings;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentData;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.ICondition;
import com.xiaohunao.heaven_destiny_moment.common.event.MomentEvent;
import com.xiaohunao.heaven_destiny_moment.common.event.PlayerMomentAreaEvent;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMAttachments;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMRegistries;
import com.xiaohunao.heaven_destiny_moment.common.network.MomentBarSyncPayload;
import com.xiaohunao.heaven_destiny_moment.common.network.MomentManagerSyncPayload;
import com.xiaohunao.heaven_destiny_moment.common.spawn_algorithm.ISpawnAlgorithm;
import com.xiaohunao.heaven_destiny_moment.common.spawn_algorithm.OpenAreaSpawnAlgorithm;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class MomentInstance<T extends Moment<?>> extends AttachmentHolder {
    private static final Logger LOGGER = LogUtils.getLogger();

    protected final Level level;
    protected final MomentType<?> type;
    protected final ResourceKey<Moment<?>> momentKey;
    protected final UUID uuid;


    protected MomentBar bar;
    protected long tick = -1L;
    protected MomentState state;
    protected Set<UUID> playerUUIDs = Sets.newHashSet();
    protected Set<Player> players = Sets.newHashSet();
    protected Set<UUID> inAreaPlayers = Sets.newHashSet();
    protected Set<Vec3> spawnPosList = Sets.newHashSet();
    protected CompoundTag persistentData = new CompoundTag();

    protected MomentInstance(MomentType<?> type, Level level, ResourceKey<Moment<?>> momentKey) {
        this.uuid = UUID.randomUUID();
        this.type = type;
        this.level = level;
        this.momentKey = momentKey;
    }

    protected MomentInstance(MomentType<?> type, UUID uuid, Level level, ResourceKey<Moment<?>> momentKey) {
        this.uuid = uuid;
        this.type = type;
        this.level = level;
        this.momentKey = momentKey;
    }

    public static boolean create(ResourceKey<Moment<?>> momentKey, ServerLevel serverLevel, @Nullable BlockPos pos, @Nullable ServerPlayer serverPlayer, @Nullable Consumer<MomentInstance<?>> modifier) {
        return Optional.ofNullable(serverLevel.registryAccess().registryOrThrow(HDMRegistries.Keys.MOMENT))
                .map(registry -> registry.get(momentKey))
                .map(moment -> moment.newMomentInstance(serverLevel, momentKey))
                .map(instance -> {
                    Optional.ofNullable(modifier).ifPresent(m -> m.accept(instance));
                    return MomentManager.of(serverLevel).addMoment(instance, serverLevel, pos, serverPlayer);
                })
                .orElse(false);
    }

    public static boolean create(ResourceKey<Moment<?>> momentKey, ServerLevel serverLevel, BlockPos pos, @Nullable ServerPlayer serverPlayer) {
        return create(momentKey,serverLevel,pos,serverPlayer,null);
    }

    public static Registry<Moment<?>> registryChecked(ResourceKey<Moment<?>> momentKey, Level level) {
        Registry<Moment<?>> registry = level.registryAccess().registryOrThrow(HDMRegistries.Keys.MOMENT);
        if (registry.getHolder(momentKey).isEmpty()) {
            HeavenDestinyMoment.LOGGER.error("Moment {} not found in registry", momentKey.location());
            return null;
        }
        return registry;
    }

    /**
     * 检查是否为指定类型的时刻
     * @param key 时刻注册键
     * @return 是否匹配
     */
    public boolean is(ResourceKey<Moment<?>> key) {
        return momentKey == key;
    }

    /**
     * 获取时刻实例
     * @return 时刻实例的Optional包装
     */
    public Optional<T> moment() {
        Registry<Moment<?>> registry = level.registryAccess().registryOrThrow(HDMRegistries.Keys.MOMENT);
        Moment<?> moment = registry.get(momentKey);
        return (Optional<T>) Optional.ofNullable(moment);
    }

    /**
     * 初始化时刻实例
     */
    public void init(){
        initMomentBar();
        initSpawnPosList();
    }

    /**
     * 初始化时刻进度条
     */
    public void initMomentBar(){
        moment().flatMap(Moment::barRenderType).ifPresent(type ->
                this.bar = new MomentBar(uuid,type));

        if (!level.isClientSide){
            PacketDistributor.sendToPlayersInDimension((ServerLevel) level,MomentBarSyncPayload.addPlayer(this.bar));
        }

    }

    /**
     * 初始化生成点列表
     */
    public void initSpawnPosList(){

    }

    /**
     * 获取随机生成点
     * @return 随机生成点坐标
     */
    public Vec3 getRandomSpawnPos() {
        if (spawnPosList.isEmpty()) {
            return Vec3.ZERO;
        }

        List<Vec3> vec3s = Lists.newArrayList(spawnPosList);
        return vec3s.get(level.random.nextInt(spawnPosList.size()));
    }


    /**
     * 更新进度条进度
     * @param progress 进度值(0-1)
     */
    public void updateBarProgress(float progress){
        if (this.bar != null){
            this.bar.updateProgress(progress);
            if (!level.isClientSide){
                PacketDistributor.sendToPlayersInDimension((ServerLevel) level,MomentBarSyncPayload.updateProgress(this.bar));
            }
        }
    }

    /**
     * 从NBT标签加载时刻实例
     * @param level 世界实例
     * @param compoundTag NBT数据
     * @return 加载的时刻实例
     */
    @Nullable
    public static MomentInstance<?> loadStatic(Level level, CompoundTag compoundTag) {
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

        serializeMetaData(compoundTag);
        serializeBar(compoundTag);
        compoundTag.put("persistentData", this.persistentData);
        compoundTag.putLong("tick", tick);
        if (state != null) {
            compoundTag.putString("state", state.name());
        }

        ListTag playerUUIDTags = new ListTag();
        playerUUIDs.forEach(uuid -> playerUUIDTags.add(StringTag.valueOf(uuid.toString())));
        compoundTag.put("player_uuids", playerUUIDTags);

        ListTag spawnPosListTag = new ListTag();
        spawnPosList.forEach(vec3 -> spawnPosListTag.add(Vec3.CODEC.encodeStart(NbtOps.INSTANCE,vec3).getOrThrow()));
        compoundTag.put("spawnPosList",spawnPosListTag);


        return compoundTag;
    }

    private void serializeBar(CompoundTag compoundTag) {
        if (this.bar != null){
            compoundTag.put("bar",MomentBar.CODEC.encodeStart(NbtOps.INSTANCE,this.bar).getOrThrow());
        }
    }

    private void deserializeBar(CompoundTag compoundTag) {
        if (compoundTag.contains("bar")){
            this.bar = MomentBar.CODEC.decode(NbtOps.INSTANCE,compoundTag.get("bar")).getOrThrow().getFirst();
        }
    }

    public void deserializeNBT(CompoundTag compoundTag) {
        deserializeBar(compoundTag);

        this.persistentData = compoundTag.getCompound("persistentData");
        this.tick = compoundTag.getLong("tick");
        if (compoundTag.contains("state")) {
            this.state = MomentState.valueOf(compoundTag.getString("state"));
        }

        ListTag playerUUIDTags = compoundTag.getList("player_uuids", Tag.TAG_LIST);
        playerUUIDTags.forEach(tag -> playerUUIDs.add(UUID.fromString(tag.getAsString())));

        ListTag spawnPosListTag = compoundTag.getList("spawnPosList", Tag.TAG_LIST);
        spawnPosListTag.forEach(tag -> spawnPosList.add(Vec3.CODEC.decode(NbtOps.INSTANCE,tag).getOrThrow().getFirst()));
    }

    private void serializeMetaData(CompoundTag compoundTag) {
        compoundTag.putUUID("uuid", uuid);
        compoundTag.putString("id", MomentInstance.getRegistryName(type).toString());
        compoundTag.put("moment", ResourceKey.codec(HDMRegistries.Keys.MOMENT).encodeStart(NbtOps.INSTANCE, momentKey).getOrThrow());
    }

    public CompoundTag getPersistentData() {
        return persistentData;
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

    public final void baseTick() {
        this.tick++;
        NeoForge.EVENT_BUS.post(new MomentEvent.Tick(this));

        if (state == MomentState.END) return;

        updatePlayers();
        updatePlayerIsInArea();
        updateConditionGroup();
        updateMomentState();

    }

    private void updateConditionGroup() {
        moment().flatMap(Moment::momentData)
                .flatMap(MomentData::conditionGroup)
                .ifPresent(conditionGroup -> {
                    checkConditionsForEachPlayer(conditionGroup.victory(), MomentState.VICTORY);
                    checkConditionsForEachPlayer(conditionGroup.end(), MomentState.END);
                    checkConditionsForEachPlayer(conditionGroup.lose(), MomentState.LOSE);
                });
    }

    private void checkConditionsForEachPlayer(Optional<List<ICondition>> conditionsOptional, MomentState state) {
        if (conditionsOptional.isEmpty()) return;

        List<ICondition> conditions = conditionsOptional.get();
        players.forEach(player -> {
            if (player instanceof ServerPlayer serverPlayer){
                BlockPos blockPos = player.blockPosition();
                boolean allConditionsMatch = conditions.stream().allMatch(condition -> condition.matches(this, blockPos,serverPlayer));
                if (allConditionsMatch) {
                    setState(state);
                }
            }
        });
    }

    private void updateMomentState() {
        if (tick == 0L) {
            MomentEvent.Ready ready = (MomentEvent.Ready)setState(MomentState.READY);
            if (ready.isCanceled()){
                setState(MomentState.END);
            }
        }

        if (state == MomentState.READY){
            ready();
        }

        if (state == MomentState.START){
            MomentEvent.Start start = (MomentEvent.Start)setState(MomentState.START);
            if (!start.isCanceled()){
                start();
                setState(MomentState.ONGOING);
            }
        }

        if (state == MomentState.ONGOING) {
            ongoing();
        }

        if (state == MomentState.VICTORY){
            MomentEvent.Victory momentEvent = (MomentEvent.Victory)setState(MomentState.VICTORY);
            if (!momentEvent.isCanceled()){
                victory();
            }
            setState(MomentState.END);
        }

        if (state == MomentState.LOSE){
            MomentEvent.Lose momentEvent = (MomentEvent.Lose)setState(MomentState.LOSE);
            if (!momentEvent.isCanceled()) {
                lose();
            }
            setState(MomentState.END);
        }
        tick();
    }


    protected void ready() {
        setState(MomentState.START);
    }

    protected void start() {

    }

    protected void ongoing() {

    }

    protected void victory() {
        moment().flatMap(Moment::momentData)
                .flatMap(MomentData::rewards)
                .ifPresent(rewards ->
                    players.forEach(player ->
                            rewards.forEach(reward ->
                                    reward.createReward(this, player)
                            )
                    )
                );
    }

    protected void lose() {

    }

    public void tick() {

    }

    public void end(){}



    public MomentEvent setState(MomentState state) {
        this.state = state;
        if (level instanceof ServerLevel serverLevel){
            PacketDistributor.sendToPlayersInDimension(serverLevel, new MomentManagerSyncPayload(this.serializeNBT()));
        }
        moment().flatMap(Moment::tipSettings).ifPresent(tip -> tip.playTooltip(this));
        return NeoForge.EVENT_BUS.post(MomentEvent.getEventToPost(this, state));
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

    public void updatePlayers() {
        if (bar != null){

        }

        final Set<Player> oldPlayers = Sets.newHashSet(players);
        final Set<Player> newPlayers = Sets.newHashSet((getPlayers(validPlayer())));
//        players.clear();


        newPlayers.stream()
                .filter(player -> !oldPlayers.contains(player))
                .forEach(player1 -> {
                    if (MomentManager.of(level).addPlayerToMoment(player1,this)) {
//                        bar.addPlayer(serverPlayer);
                        players.add(player1);
                        playerUUIDs.add(player1.getUUID());
                        if (this.bar != null){
                            this.bar.addPlayer(player1);
                        }
                    }
                });
        oldPlayers.stream()
                .filter(player -> !newPlayers.contains(player))
                .forEach(player1 -> {

                    if (MomentManager.of(level).removePlayerToMoment(player1,this)) {
//                      bar.removePlayer(player1);
                        players.remove(player1);
                        playerUUIDs.add(player1.getUUID());
                        if (this.bar != null){
                            this.bar.removePlayer(player1);
                        }
                    }
                });
    }

    private void updatePlayerIsInArea() {
        if (level.isClientSide) return;

        players.stream().filter(Objects::nonNull).forEach(player ->
            moment().ifPresent(moment -> {
                boolean inArea = moment.isInArea((ServerLevel) level, player.blockPosition());
                boolean uuidContains = inAreaPlayers.contains(player.getUUID());
                if (inArea && !uuidContains) {
                    onPlayerEnterArea((ServerPlayer) player);
                } else if (!inArea && uuidContains) {
                    onPlayerExitArea((ServerPlayer) player);
                }
            }));
    }

    private void onPlayerExitArea(ServerPlayer player) {
        inAreaPlayers.remove(player.getUUID());
        NeoForge.EVENT_BUS.post(new PlayerMomentAreaEvent.Exit(player, this));
    }

    private void onPlayerEnterArea(ServerPlayer player) {
        inAreaPlayers.add(player.getUUID());
        NeoForge.EVENT_BUS.post(new PlayerMomentAreaEvent.Enter(player, this));
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

    public MomentType<?> getType() {
        return type;
    }

    public void finalizeSpawn(Entity entity) {
    }

    public void addKillCount(LivingEntity livingEntity) {
        this.setData(HDMAttachments.MOMENT_KILL_ENTITY,getData(HDMAttachments.MOMENT_KILL_ENTITY).addKillCount(livingEntity));
    }

    public void livingDeath(LivingEntity entity) {

    }

    public boolean canCreate(Map<UUID, MomentInstance<?>> runMoments, ServerLevel serverLevel, @Nullable BlockPos pos,@Nullable ServerPlayer player) {
        return true;
    }

    public Player getRandomPlayer() {
        if (players.isEmpty()){
            return null;
        }

        List<Player> playerList = Lists.newArrayList(players);
        return playerList.get(level.random.nextInt(playerList.size()));
    }

    public boolean isClientOnlyMoment() {
        return moment().map(Moment::isClientOnlyMoment).orElse(false);
    }

    public boolean canSpawnEntity(Level level, Entity entity, BlockPos pos){
        return true;
    }

    public void setEntityTagMark(Entity entity){
        entity.setData(HDMAttachments.MOMENT_ENTITY,entity.getData(HDMAttachments.MOMENT_ENTITY).setUid(this.uuid));
    }

    public void setSpawnPos(Entity entity){
        ISpawnAlgorithm spawnAlgorithm = moment()
                .flatMap(Moment::momentData)
                .flatMap(MomentData::entitySpawnSettings)
                .flatMap(EntitySpawnSettings::spawnAlgorithm)
                .orElse(OpenAreaSpawnAlgorithm.DEFAULT);
        entity.setPos(spawnAlgorithm.spawn(this,entity));
    }

    public void spawnEntity(Entity entity) {
        setEntityTagMark(entity);
        setSpawnPos(entity);
        finalizeSpawn(entity);
        level.addFreshEntity(entity);
    }
}
