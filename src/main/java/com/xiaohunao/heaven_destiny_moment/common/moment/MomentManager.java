package com.xiaohunao.heaven_destiny_moment.common.moment;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.mixed.MomentManagerContainer;
import com.xiaohunao.heaven_destiny_moment.common.network.MomentManagerSyncPayload;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.network.PacketDistributor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class MomentManager extends SavedData {
    private static final String NAME = HeavenDestinyMoment.MODID + "_moment_manager";

    private final Map<UUID, MomentInstance> runMoments = Maps.newHashMap();
    private MomentInstance onlyMoment = null;
    private final Multimap<UUID,MomentInstance> playerMoments = HashMultimap.create();

    private static MomentManager clientMonger;


    public static MomentManager of(Level level) {
        MomentManagerContainer container = (MomentManagerContainer)level;
        MomentManager momentManager = container.heaven_destiny_moment$getMomentManager();
        if (level instanceof ServerLevel serverLevel) {
            if (momentManager == null) {
                momentManager = serverLevel.getDataStorage().computeIfAbsent(new Factory<>(MomentManager::new,
                        (CompoundTag compoundTag, HolderLookup.Provider tag) -> load(serverLevel, compoundTag, tag)), NAME);
                container.heaven_destiny_moment$setMomentManager(momentManager);
            }
            return momentManager;
        }else {
            if (clientMonger == null) {
                clientMonger = new MomentManager();
            }
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

    public static MomentManager load(ServerLevel level,CompoundTag tag, HolderLookup.Provider lookupProvider) {
        MomentManager manager = new MomentManager();
        ListTag listTag = tag.getList("moment", Tag.TAG_COMPOUND);
        listTag.forEach(compoundTag -> {
            manager.addMoment(level,MomentInstance.loadStatic(level, (CompoundTag) compoundTag));
            PacketDistributor.sendToPlayersInDimension(level,new MomentManagerSyncPayload((CompoundTag) compoundTag));
        });
        return manager;
    }

    public void tick() {
        CopyOnWriteArrayList<MomentInstance> momentInstances = new CopyOnWriteArrayList<>(runMoments.values());
        momentInstances.forEach(instance -> {
            if (instance.shouldEnd()) {
                removeMoment(instance.getID());
            }
            instance.baseTick();
        });
    }

    public Map<UUID, MomentInstance> getRunMoment() {
        return this.runMoments;
    }

    public void removeMoment(UUID uuid) {
        runMoments.remove(uuid);
    }

    public boolean addMoment(ServerLevel serverLevel,MomentInstance instance) {
        UUID uuid = instance.getID();
//        boolean hasClientSettings = !instance.getMoment().getClientSettingsContext().isEmpty();
//
//        if (runMoments.containsKey(uuid)) {
//            return false;
//        }
//
//        if (hasClientSettings) {
//            if (onlyMoment != null) {
//                return false;
//            } else {
//                onlyMoment = instance;
//            }
//        }

        runMoments.put(uuid, instance);
        PacketDistributor.sendToPlayersInDimension(serverLevel,new MomentManagerSyncPayload(instance.serializeNBT()));
        setDirty();
        return true;
    }

    @Nullable
    public MomentInstance getOnlyMoment() {
        return onlyMoment;
    }

    public Collection<MomentInstance> getPlayerMoment(UUID playerUUID) {
        return playerMoments.get(playerUUID);
    }

    public Map<UUID, MomentInstance> getRunMoments() {
        return runMoments;
    }
}
