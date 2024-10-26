package com.xiaohunao.heaven_destiny_moment.common.moment;

import com.google.common.collect.Maps;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.mixed.MomentManagerContainer;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.Map;
import java.util.UUID;

public class MomentManager extends SavedData {
    private static final String NAME = HeavenDestinyMoment.MODID + "_moment_manager";

    private final Map<UUID, MomentInstance> runMoment = Maps.newHashMap();


    public static MomentManager of(ServerLevel level) {
        MomentManagerContainer container = (MomentManagerContainer)level;
        MomentManager momentManager = container.heaven_destiny_moment$getMomentManager();

        if (momentManager == null) {
            momentManager = level.getDataStorage().computeIfAbsent(new Factory<>(MomentManager::new,
                    (CompoundTag compoundTag, HolderLookup.Provider tag) -> load(level,compoundTag, tag)), NAME);
            container.heaven_destiny_moment$setMomentManager(momentManager);
        }
        return momentManager;
    }


    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        ListTag listTag = new ListTag();
        runMoment.values().forEach(momentInstance -> listTag.add(momentInstance.serializeNBT()));
        compoundTag.put("moment", listTag);
        return compoundTag;
    }

    public static MomentManager load(ServerLevel serverLevel,CompoundTag tag, HolderLookup.Provider lookupProvider) {
        MomentManager manager = new MomentManager();
        ListTag listTag = tag.getList("moment", Tag.TAG_COMPOUND);
        listTag.forEach(compoundTag -> {
            manager.addMoment(serverLevel,MomentInstance.loadStatic(serverLevel, (CompoundTag) compoundTag));
        });
        return manager;
    }
    public void addMoment(ServerLevel serverLevel,MomentInstance instance) {
        runMoment.put(instance.getID(), instance);
        setDirty();
    }

    public void tick() {
        System.out.println(runMoment);
        setDirty();
    }
}
