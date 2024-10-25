package com.xiaohunao.heaven_destiny_moment.common.attachment;

import com.google.common.collect.Maps;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class MomentAttachment implements INBTSerializable<CompoundTag> {
    private final Map<UUID, MomentInstance> runMoment = Maps.newHashMap();



    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag compoundTag = new CompoundTag();
        ListTag listTag = new ListTag();
        runMoment.values().forEach(momentInstance -> listTag.add(momentInstance.serializeNBT()));
        compoundTag.put("moment", listTag);
        return compoundTag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compoundTag) {
        ListTag listTag = compoundTag.getList("moment", Tag.TAG_COMPOUND);
        listTag.forEach(tag -> {
            //TODO 待序列化
//            MomentInstance momentInstance = MomentInstance.loadStatic();
        });
    }
}
