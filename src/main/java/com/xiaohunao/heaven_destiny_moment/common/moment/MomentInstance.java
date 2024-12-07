package com.xiaohunao.heaven_destiny_moment.common.moment;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.client.gui.bar.MomentBar;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentDataContext;
import com.xiaohunao.heaven_destiny_moment.common.event.MomentEvent;
import com.xiaohunao.heaven_destiny_moment.common.event.PlayerMomentAreaEvent;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMAttachments;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
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
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class MomentInstance<T extends Moment> extends AttachmentHolder {
    private static final Logger LOGGER = LogUtils.getLogger();

    protected final Level level;
    protected final MomentType<?> type;
    protected final ResourceKey<Moment> momentKey;
    protected final UUID uuid;


    protected MomentBar bar;
    protected long tick = -1L;
    protected MomentState state;
    protected final Set<UUID> playerUUIDs = Sets.newHashSet();
    protected final Set<Player> players = Sets.newHashSet();
    protected final Set<UUID> inAreaPlayers = Sets.newHashSet();

    protected MomentInstance(MomentType<?> type, Level level, ResourceKey<Moment> momentKey) {
        this.uuid = UUID.randomUUID();
        this.type = type;
        this.level = level;
        this.momentKey = momentKey;
    }

    protected MomentInstance(MomentType<?> type, UUID uuid, Level level, ResourceKey<Moment> momentKey) {
        this.uuid = uuid;
        this.type = type;
        this.level = level;
        this.momentKey = momentKey;
    }

    public static boolean create(ResourceKey<Moment> momentKey, ServerLevel serverLevel, BlockPos pos, ServerPlayer serverPlayer, @Nullable Consumer<MomentInstance> modifier) {
        Registry<Moment> registry = serverLevel.registryAccess().registryOrThrow(HDMRegistries.Keys.MOMENT);
        return Optional.ofNullable(registry.get(momentKey))
                .map(moment -> moment.newMomentInstance(serverLevel,momentKey))
                .map(instance -> {
                    if (modifier != null) {
                        modifier.accept(instance);
                    }
                    return MomentManager.of(serverLevel).addMoment(instance,serverLevel,pos,serverPlayer);
                }).orElse(false);
    }
    public static boolean create(ResourceKey<Moment> momentKey, ServerLevel serverLevel, BlockPos pos, ServerPlayer serverPlayer) {
        return create(momentKey,serverLevel,pos,serverPlayer,null);
    }

    public static Registry<Moment> registryChecked(ResourceKey<Moment> momentKey, Level level) {
        Registry<Moment> registry = level.registryAccess().registryOrThrow(HDMRegistries.Keys.MOMENT);
        if (registry.getHolder(momentKey).isEmpty()) {
            HeavenDestinyMoment.LOGGER.error("Moment {} not found in registry", momentKey.location());
            return null;
        }
        return registry;
    }

    public Optional<T> moment() {
        Registry<Moment> registry = level.registryAccess().registryOrThrow(HDMRegistries.Keys.MOMENT);
        Moment moment = registry.get(momentKey);
        return Optional.ofNullable((T) moment);
    }
    public void init(){
        initMomentBar();
    }
    public void initMomentBar(){
        moment().flatMap(Moment::barRenderType).ifPresent(type -> {
            this.bar = new MomentBar(uuid,type);
        });
    }


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

    public final void baseTick() {
        this.tick++;
        NeoForge.EVENT_BUS.post(new MomentEvent.Tick(this));

        if (state == MomentState.END) return;

        updatePlayers();
        updatePlayerIsInArea();
        updateMomentState();



    }

    private void updateMomentState() {
        if (tick == 0L) {
            MomentEvent.Ready ready = (MomentEvent.Ready)setState(MomentState.READY);
            if (!ready.isCanceled()){
                ready();
            }else {
                setState(MomentState.END);
            }
        }

        if (state == MomentState.READY){
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
    }


    protected void ready() {

    }

    protected void start() {

    }

    protected void ongoing() {
        if (!players.isEmpty() && state == MomentState.ONGOING) {
            tick();
        }
    }

    protected void victory() {
        moment().flatMap(Moment::momentDataContext)
                .flatMap(MomentDataContext::rewards)
                .ifPresent(rewards -> {
                    players.forEach(player -> {
                            rewards.forEach(reward -> {
                                reward.createReward(this, player);
                            });
                        });
                });
    }

    protected void lose() {

    }

    public void tick() {

    }



    public MomentEvent setState(MomentState state) {
        this.state = state;
        moment().flatMap(Moment::tipSettingsContext).ifPresent(tip -> tip.playTooltip(this));
        return NeoForge.EVENT_BUS.post(MomentEvent.getEventToPost(this, state));
    }


    public Predicate<Player> validPlayer() {
        return player -> !player.isSpectator();
    }

    public boolean canSpawnEntity(ServerLevel serverLevel, Entity entity, BlockPos pos){
        return true;
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
        if (bar != null){

        }

        final Set<Player> oldPlayers = Sets.newHashSet(players);
        final Set<Player> newPlayers = Sets.newHashSet((getPlayers(validPlayer())));
//        players.clear();


        newPlayers.stream()
                .filter(player -> !oldPlayers.contains(player))
                .forEach(serverPlayer -> {
                    if (MomentManager.of(level).addPlayerToMoment(serverPlayer,this)) {
//                        bar.addPlayer(serverPlayer);
                        players.add(serverPlayer);
                        playerUUIDs.add(serverPlayer.getUUID());
                    }
                });
        oldPlayers.stream()
                .filter(player -> !newPlayers.contains(player))
                .forEach(player1 -> {
//                    bar.removePlayer(player1);
                    players.remove(player1);
                    playerUUIDs.add(player1.getUUID());
                });
//        bar.getPlayers().forEach(player -> {
//            playerUUIDs.add(player.getUUID());
//            players.add(player);
//        });


    }

    private void updatePlayerIsInArea() {
        if (level.isClientSide) return;

        players.stream().filter(Objects::nonNull).forEach(player -> {
            moment().ifPresent(moment -> {
                boolean inArea = moment.isInArea((ServerLevel) level, player.blockPosition());
                boolean uuidContains = inAreaPlayers.contains(player.getUUID());
                if (inArea && !uuidContains) {
                    onPlayerEnterArea((ServerPlayer) player);
                } else if (!inArea && uuidContains) {
                    onPlayerExitArea((ServerPlayer) player);
                }
            });
        });
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

    public void finalizeSpawn(Entity entity) {

    }

    public void addKillCount(LivingEntity livingEntity) {
        this.setData(HDMAttachments.MOMENT_KILL_ENTITY,getData(HDMAttachments.MOMENT_KILL_ENTITY).addKillCount(livingEntity));
    }

    public void livingDeath(LivingEntity entity) {

    }

    public boolean canCreate(Map<UUID, MomentInstance<?>> runMoments, ServerLevel serverLevel, BlockPos pos,@Nullable ServerPlayer player) {
        return true;
    }

    public Player getRandomPlayer() {
        if (players.isEmpty()){
            return null;
        }

        List<Player> playerList = Lists.newArrayList(players);
        return playerList.get(level.random.nextInt(playerList.size()));
    }
}
