package com.xiaohunao.heaven_destiny_moment.common.context.predicate;

import com.mojang.serialization.Codec;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecMap;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecProvider;

public abstract class PredicateContext implements CodecProvider<PredicateContext> {
    public static final CodecMap<PredicateContext> CODEC = new CodecMap<>("Predicate");
    public static void register() {
        CODEC.register(AttributePredicateContext.ID, AttributePredicateContext.CODEC);
        CODEC.register(EffectPredicateContext.ID, EffectPredicateContext.CODEC);
        CODEC.register(EquipmentPredicateContext.ID, EquipmentPredicateContext.CODEC);
        CODEC.register(TagPredicateContext.ID, TagPredicateContext.CODEC);
    }

    public abstract Codec<? extends PredicateContext> getCodec();
}
