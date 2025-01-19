package com.xiaohunao.heaven_destiny_moment.common.moment;

import com.google.common.collect.*;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.context.ClientSettings;
import com.xiaohunao.heaven_destiny_moment.common.context.ConditionGroup;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentData;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.common.AutoProbabilityCondition;
import com.xiaohunao.heaven_destiny_moment.common.mixed.MomentManagerContainer;
import com.xiaohunao.heaven_destiny_moment.common.network.ClientOnlyMomentSyncPayload;
import com.xiaohunao.heaven_destiny_moment.common.network.MomentBarSyncPayload;
import com.xiaohunao.heaven_destiny_moment.common.network.MomentManagerSyncPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class MomentManager extends SavedData {
    private static final String NAME = HeavenDestinyMoment.MODID + "_moment_manager";

    private final Map<UUID, MomentInstance<?>> runMoments = Maps.newHashMap();
    private final Set<ResourceKey<Moment<?>>> runMomentKeyes = Sets.newHashSet();
    private MomentInstance<?> clientOnlyMoment = null;
    private final Multimap<UUID, MomentInstance<?>> playerMoments = HashMultimap.create();

    private MomentManager clientMonger;
    private Level level;


    public static MomentManager of(Level level) {
        MomentManagerContainer container = (MomentManagerContainer) level;
        MomentManager momentManager = container.heaven_destiny_moment$getMomentManager();
        if (level instanceof ServerLevel serverLevel) {
            if (momentManager == null) {
                momentManager = serverLevel.getDataStorage().computeIfAbsent(new Factory<>(MomentManager::new,
                        (CompoundTag compoundTag, HolderLookup.Provider tag) -> load(serverLevel, compoundTag, tag)), NAME);
                container.heaven_destiny_moment$setMomentManager(momentManager);
                momentManager.level = serverLevel;
            }
        } else {
            if (momentManager == null) {
                momentManager = new MomentManager();
                container.heaven_destiny_moment$setMomentManager(momentManager);
            }
            momentManager.level = level;
        }
        return momentManager;
    }


    @Override
    public @NotNull CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        ListTag listTag = new ListTag();
        runMoments.values().forEach(momentInstance -> listTag.add(momentInstance.serializeNBT()));
        compoundTag.put("moment", listTag);
        return compoundTag;
    }

    public static MomentManager load(ServerLevel serverLevel, CompoundTag tag, HolderLookup.Provider lookupProvider) {
        MomentManager manager = new MomentManager();
        ListTag listTag = tag.getList("moment", Tag.TAG_COMPOUND);
        listTag.forEach(compoundTag -> {
            MomentInstance<?> instance = MomentInstance.loadStatic(serverLevel, (CompoundTag) compoundTag);
            if (instance != null) {
                UUID uuid = instance.getID();
                manager.runMoments.put(uuid, instance);
                manager.runMomentKeyes.add(instance.momentKey);
                PacketDistributor.sendToPlayersInDimension(serverLevel, new MomentManagerSyncPayload(instance.serializeNBT()));
                if (instance.getBar() != null) {
                    PacketDistributor.sendToPlayersInDimension(serverLevel, MomentBarSyncPayload.addPlayer(instance.getBar()));
                }
            }
        });
        return manager;
    }

    public void tick() {
        CopyOnWriteArrayList<MomentInstance<?>> momentInstances = new CopyOnWriteArrayList<>(runMoments.values());
        momentInstances.forEach(instance -> {
            if (instance.state == MomentState.END) {
                instance.end();

                if (!level.isClientSide) {
                    ServerLevel serverLevel = (ServerLevel) instance.getLevel();
                    removeMoment(instance);
                    PacketDistributor.sendToPlayersInDimension(serverLevel, new ClientOnlyMomentSyncPayload(instance.serializeNBT(), true));
                    if (instance.getBar() != null) {
                        PacketDistributor.sendToPlayersInDimension(serverLevel, MomentBarSyncPayload.removePlayer(instance.bar));
                    }
                }
                runMoments.remove(instance.getID());
                runMomentKeyes.remove(instance.momentKey);
            }
            instance.baseTick();
        });
        setDirty();
    }

    public void removeMoment(MomentInstance<?> instance) {
        runMoments.remove(instance.getID());
        runMomentKeyes.remove(instance.momentKey);
        instance.players.forEach(player -> {
            if (playerMoments.containsKey(player.getUUID())) {
                playerMoments.remove(player.getUUID(), instance);
            }
        });
        PacketDistributor.sendToPlayersInDimension((ServerLevel) level, new MomentManagerSyncPayload(instance.serializeNBT()));
    }

    public boolean addMoment(MomentInstance<?> instance, ServerLevel serverLevel, @Nullable BlockPos pos, @Nullable ServerPlayer serverPlayer) {
        UUID uuid = instance.getID();

        instance.updatePlayers();

        Boolean conditionMatch = instance.moment()
                .flatMap(Moment::momentData)
                .flatMap(MomentData::conditionGroup)
                .flatMap(ConditionGroup::create)
                .map(createGroup -> {
                    return createGroup.getSecond().stream()
                            .filter(condition -> !(condition instanceof AutoProbabilityCondition))
                            .allMatch(condition -> condition.matches(instance, pos, serverPlayer));
                })
                .orElse(true);
        boolean canCreate = instance.canCreate(runMoments, serverLevel, pos, serverPlayer);
        if (canCreate && conditionMatch) {
            instance.init();
            PacketDistributor.sendToPlayersInDimension(serverLevel, new MomentManagerSyncPayload(instance.serializeNBT()));
            runMoments.put(uuid, instance);
            runMomentKeyes.add(instance.momentKey);
            setDirty();
            return true;
        }
        return false;
    }

    public boolean addPlayerToMoment(Player player, MomentInstance<?> instance) {
        UUID playerUUID = player.getUUID();

        if (!playerMoments.containsKey(playerUUID)) {
            playerMoments.put(playerUUID, instance);
            if (!instance.getLevel().isClientSide && instance.isClientOnlyMoment()) {
                PacketDistributor.sendToPlayersInDimension((ServerLevel) instance.getLevel(), new ClientOnlyMomentSyncPayload(instance.serializeNBT(), false));
            }
            setDirty();
            return true;
        }

        boolean canAddPlayer = true;
        for (MomentInstance<?> momentInstance : playerMoments.get(playerUUID)) {
            Optional<? extends Moment<?>> moment = momentInstance.moment();
            if (moment.isPresent()) {
                if (momentInstance.getID().equals(instance.getID())) break;

                Boolean isEmpty = moment.flatMap(Moment::clientSettings).map(ClientSettings::isPresent).orElse(true);

                if (!isEmpty) {
                    canAddPlayer = false;
                    break;
                }
            }
        }

        if (canAddPlayer) {
            playerMoments.put(playerUUID, instance);
            Boolean isEmpty = instance.moment().flatMap(Moment::clientSettings).map(ClientSettings::isPresent).orElse(true);
            if (isEmpty) {
                if (!instance.getLevel().isClientSide && instance.isClientOnlyMoment()) {
                    PacketDistributor.sendToPlayersInDimension((ServerLevel) instance.getLevel(), new ClientOnlyMomentSyncPayload(instance.serializeNBT(), false));
                }
            }
            setDirty();
            return true;
        }

        return false;
    }

    public boolean removePlayerToMoment(Player player, MomentInstance<?> instance) {
        UUID playerUUID = player.getUUID();
        if (playerMoments.containsKey(playerUUID)) {
            playerMoments.remove(playerUUID, instance);
            setDirty();
            return true;
        }
        return false;
    }

    public MomentManager setClientOnlyMoment(MomentInstance<?> clientOnlyMoment) {
        this.clientOnlyMoment = clientOnlyMoment;
        setDirty();
        return this;
    }

    public Optional<MomentInstance<?>> getClientOnlyMoment() {
        return Optional.ofNullable(clientOnlyMoment);
    }

    public Collection<MomentInstance<?>> getPlayerMoment(UUID playerUUID) {
        return playerMoments.get(playerUUID);
    }

    public Map<UUID, MomentInstance<?>> getImmutableRunMoments() {
        return ImmutableMap.copyOf(runMoments);
    }

    public Map<UUID, MomentInstance<?>> getRunMoments() {
        return runMoments;
    }

    public boolean hasMoment(ResourceKey<Moment<?>> key) {
        return runMomentKeyes.contains(key);
    }

    public MomentInstance<?> getMomentInstance(UUID uuid){
        return runMoments.get(uuid);
    }
}
