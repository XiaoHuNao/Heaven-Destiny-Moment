package com.xiaohunao.heaven_destiny_moment.common.moment;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.context.ClientSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.mixed.MomentManagerContainer;
import com.xiaohunao.heaven_destiny_moment.common.network.ClientOnlyMomentSyncPayload;
import com.xiaohunao.heaven_destiny_moment.common.network.MomentManagerSyncPayload;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class MomentManager extends SavedData {
    private static final String NAME = HeavenDestinyMoment.MODID + "_moment_manager";

    private final Map<UUID, MomentInstance> runMoments = Maps.newHashMap();
    private MomentInstance clientOnlyMoment = null;
    private final Multimap<UUID, Pair<UUID,MomentInstance>> playerMoments = HashMultimap.create();

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

    public static MomentManager load(ServerLevel level, CompoundTag tag, HolderLookup.Provider lookupProvider) {
        MomentManager manager = new MomentManager();
        ListTag listTag = tag.getList("moment", Tag.TAG_COMPOUND);
        listTag.forEach(compoundTag -> {
            manager.addMoment(level, Objects.requireNonNull(MomentInstance.loadStatic(level, (CompoundTag) compoundTag)));
            PacketDistributor.sendToPlayersInDimension(level, new MomentManagerSyncPayload((CompoundTag) compoundTag,false));
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

    public Map<UUID, MomentInstance> getRunMoment() {
        return this.runMoments;
    }

    public void removeMoment(MomentInstance instance) {
        runMoments.remove(instance.getID());
        PacketDistributor.sendToPlayersInDimension((ServerLevel) level, new MomentManagerSyncPayload(instance.serializeNBT(),false));
    }

    public void addMoment(ServerLevel serverLevel, MomentInstance instance) {
        UUID uuid = instance.getID();

        runMoments.put(uuid, instance);
        PacketDistributor.sendToPlayersInDimension(serverLevel, new MomentManagerSyncPayload(instance.serializeNBT(),false));
        setDirty();
    }
    public boolean addPlayerToMoment(Player player, MomentInstance instance) {
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
        for (Pair<UUID, MomentInstance> uuidMomentInstancePair : playerMoments.get(playerUUID)) {
            MomentInstance second = uuidMomentInstancePair.getSecond();
            Optional<Moment> moment = second.getMoment();
            if (moment.isPresent()) {
                ClientSettingsContext clientSettingsContext = moment.get().getClientSettingsContext();
                if (second.getID().equals(instance.getID())) break;

                if (!clientSettingsContext.isEmpty()) {
                    canAddPlayer = false;
                    break;
                }
            }
        }

        if (canAddPlayer) {
            playerMoments.put(playerUUID, new Pair<>(instance.getID(), instance));
            if (instance.getMoment().get().getClientSettingsContext().isEmpty()) {
                if (!instance.getLevel().isClientSide) {
                    PacketDistributor.sendToPlayersInDimension((ServerLevel) instance.getLevel(), new ClientOnlyMomentSyncPayload(instance.serializeNBT()));
                }
            }
            setDirty();
            return true;
        }

        return false;
    }

    public MomentManager setClientOnlyMoment(MomentInstance clientOnlyMoment) {
        this.clientOnlyMoment = clientOnlyMoment;
        setDirty();
        return this;
    }

    public Optional<MomentInstance> getClientOnlyMoment() {
        return Optional.ofNullable(clientOnlyMoment);
    }

    public Collection<Pair<UUID, MomentInstance>> getPlayerMoment(UUID playerUUID) {
        return playerMoments.get(playerUUID);
    }

    public Map<UUID, MomentInstance> getRunMoments() {
        return runMoments;
    }
}
