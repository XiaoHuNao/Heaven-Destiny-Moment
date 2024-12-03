package com.xiaohunao.heaven_destiny_moment.common.moment;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.context.ClientSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentDataContext;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.IConditionContext;
import com.xiaohunao.heaven_destiny_moment.common.mixed.MomentManagerContainer;
import com.xiaohunao.heaven_destiny_moment.common.network.ClientOnlyMomentSyncPayload;
import com.xiaohunao.heaven_destiny_moment.common.network.MomentManagerSyncPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class MomentManager extends SavedData {
    private static final String NAME = HeavenDestinyMoment.MODID + "_moment_manager";

    private final Map<UUID, MomentInstance<?>> runMoments = Maps.newHashMap();
    private MomentInstance<?> clientOnlyMoment = null;
    private final Multimap<UUID, Pair<UUID,MomentInstance<?>>> playerMoments = HashMultimap.create();

    private static MomentManager clientMonger;
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
            return momentManager;
        } else {
            if (clientMonger == null) {
                clientMonger = new MomentManager();
            }
            clientMonger.level = level;
            return clientMonger;
        }
    }


    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        ListTag listTag = new ListTag();
        runMoments.values().forEach(momentInstance -> listTag.add(momentInstance.serializeNBT()));
        compoundTag.put("moment", listTag);
        return compoundTag;
    }

    public static MomentManager load(ServerLevel serverLevel, CompoundTag tag, HolderLookup.Provider lookupProvider) {
        MomentManager manager = new MomentManager();
        ListTag listTag = tag.getList("moment", Tag.TAG_COMPOUND);
        listTag.forEach(compoundTag -> {
            MomentInstance instance = MomentInstance.loadStatic(serverLevel, (CompoundTag) compoundTag);
            if (instance != null) {
                UUID uuid = instance.getID();
                manager.runMoments.put(uuid, instance);
                PacketDistributor.sendToPlayersInDimension(serverLevel, new MomentManagerSyncPayload(instance.serializeNBT(),false));
                manager.setDirty();
            }
        });
        return manager;
    }

    public void tick() {
        CopyOnWriteArrayList<MomentInstance> momentInstances = new CopyOnWriteArrayList<>(runMoments.values());
        momentInstances.forEach(instance -> {
            if (instance.state == MomentState.END) {
                if (!level.isClientSide){
                    removeMoment(instance);
                }
                runMoments.remove(instance.getID());
            }
            instance.baseTick();
        });
    }

    public Map<UUID, MomentInstance<?>> getRunMoment() {
        return this.runMoments;
    }

    public void removeMoment(MomentInstance<?> instance) {
        runMoments.remove(instance.getID());
        PacketDistributor.sendToPlayersInDimension((ServerLevel) level, new MomentManagerSyncPayload(instance.serializeNBT(),false));
    }

    public boolean addMoment(MomentInstance<?> instance, ServerLevel serverLevel, BlockPos pos, @Nullable ServerPlayer serverPlayer) {
        UUID uuid = instance.getID();
        Boolean conditionMatch = instance.moment()
                .flatMap(Moment::momentDataContext)
                .flatMap(MomentDataContext::conditions)
                .map(set -> set.stream().anyMatch(condition -> condition.matches(instance, pos)))
                .orElse(true);
        if (instance.canCreate(runMoments,serverLevel,pos,serverPlayer) && conditionMatch) {
            instance.init();
            runMoments.put(uuid, instance);
            PacketDistributor.sendToPlayersInDimension(serverLevel, new MomentManagerSyncPayload(instance.serializeNBT(),false));
            setDirty();
            return true;
        }
        return false;
    }
    public boolean addPlayerToMoment(Player player, MomentInstance<?> instance) {
        UUID playerUUID = player.getUUID();

        if (!playerMoments.containsKey(playerUUID)) {
            playerMoments.put(playerUUID, new Pair<>(instance.getID(), instance));
            if (!instance.getLevel().isClientSide) {
                PacketDistributor.sendToPlayersInDimension((ServerLevel) instance.getLevel(), new ClientOnlyMomentSyncPayload(instance.serializeNBT()));
            }
            setDirty();
            return true;
        }

        boolean canAddPlayer = true;
        for (Pair<UUID, MomentInstance<?>> uuidMomentInstancePair : playerMoments.get(playerUUID)) {
            MomentInstance<?> second = uuidMomentInstancePair.getSecond();
            Optional<? extends Moment> moment = second.moment();
            if (moment.isPresent()) {
                if (second.getID().equals(instance.getID())) break;

                Boolean isEmpty = moment.flatMap(Moment::clientSettingsContext).map(ClientSettingsContext::isEmpty).orElse(true);

                if (!isEmpty) {
                    canAddPlayer = false;
                    break;
                }
            }
        }

        if (canAddPlayer) {
            playerMoments.put(playerUUID, new Pair<>(instance.getID(), instance));
            Boolean isEmpty = instance.moment().flatMap(Moment::clientSettingsContext).map(ClientSettingsContext::isEmpty).orElse(true);
            if (isEmpty) {
                if (!instance.getLevel().isClientSide) {
                    PacketDistributor.sendToPlayersInDimension((ServerLevel) instance.getLevel(), new ClientOnlyMomentSyncPayload(instance.serializeNBT()));
                }
            }
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

    public Collection<Pair<UUID, MomentInstance<?>>> getPlayerMoment(UUID playerUUID) {
        return playerMoments.get(playerUUID);
    }

    public Map<UUID, MomentInstance<?>> getRunMoments() {
        return ImmutableMap.copyOf(runMoments);
    }
}
