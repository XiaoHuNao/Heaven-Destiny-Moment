package com.xiaohunao.heaven_destiny_moment.common.utils;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

public class HolderUtils {
    public static <T> HolderSet<T> getHolderSet(HolderGetter<T> holderGetter, TagKey<T> tagKey) {
        return holderGetter.getOrThrow(tagKey);
    }
    @SafeVarargs
    public static <T> HolderSet<T> getHolderSet(HolderGetter<T> holderGetter, ResourceKey<T>... resourceKey) {
        Holder<T>[] holders = new Holder[resourceKey.length];
        for (int i = 0; i < resourceKey.length; i++) {
            holders[i] = holderGetter.getOrThrow(resourceKey[i]);
        }
        return HolderSet.direct(holders);
    }
}

//    public static HolderSet<Structure> getHolderSet(HolderGetter<Structure> structureHolderGetter, ResourceKey<Structure>... resourceKey) {
//    }
