package com.xiaohunao.heaven_destiny_moment.common.context.predicate;

import com.mojang.serialization.Codec;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class TagPredicateContext extends PredicateContext {
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("tag");
    public static final Codec<TagPredicateContext> CODEC = CompoundTag.CODEC.xmap(TagPredicateContext::new, TagPredicateContext::getNBT);

    public CompoundTag nbt;

    public TagPredicateContext(CompoundTag nbt) {
        this.nbt = nbt;
    }

    public CompoundTag getNBT() {
        return nbt;
    }
    public<T> TagPredicateContext add(String key, T value) {
        if (value instanceof Integer integer) {
            nbt.putInt(key, integer);
        } else if (value instanceof String string) {
            nbt.putString(key, string);
        } else if (value instanceof Boolean bool) {
            nbt.putBoolean(key, bool);
        } else if (value instanceof Float float1) {
            nbt.putFloat(key, float1);
        } else if (value instanceof Double double1) {
            nbt.putDouble(key, double1);
        } else if (value instanceof Byte byte1) {
            nbt.putByte(key, byte1);
        } else if (value instanceof Short short1) {
            nbt.putShort(key, short1);
        } else if (value instanceof Long long1) {
            nbt.putLong(key, long1);
        } else if (value instanceof CompoundTag tag) {
            nbt.put(key, tag);
        } else if (value instanceof int[] ints) {
            nbt.putIntArray(key, ints);
        } else if (value instanceof long[] longs) {
            nbt.putLongArray(key, longs);
        } else if (value instanceof byte[] bytes) {
            nbt.putByteArray(key, bytes);
        }
        return this;
    }
    @Override
    public Codec<TagPredicateContext> getCodec() {
        return CODEC;
    }
}
