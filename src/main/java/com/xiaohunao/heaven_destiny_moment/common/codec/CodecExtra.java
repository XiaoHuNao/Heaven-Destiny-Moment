package com.xiaohunao.heaven_destiny_moment.common.codec;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;

import java.util.Set;

public class CodecExtra {
    //ComponentSerialization
    //UUIDUtil
    public static <T> Codec<Set<T>> setOf(Codec<T> codec) {
        return Codec.list(codec).xmap(Sets::newLinkedHashSet, Lists::newArrayList);
    }

}