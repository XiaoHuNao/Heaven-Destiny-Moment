package com.xiaohunao.heaven_destiny_moment.common.context.predicate;

import com.mojang.serialization.Codec;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecExtra;
import com.xiaohunao.heaven_destiny_moment.common.context.WeightedContext;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public class EffectPredicateContext extends PredicateContext {
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("effect");
    public static final Codec<EffectPredicateContext> CODEC = WeightedContext.codec(MobEffectInstance.CODEC)
            .fieldOf("effects")
            .xmap(EffectPredicateContext::new, EffectPredicateContext::getEffects)
            .codec();


    public WeightedContext<MobEffectInstance> effectInstances = WeightedContext.create();
    public EffectPredicateContext(Holder<MobEffect> effect, int duration, int amplifier, int weight) {
        addEffect(effect, duration, amplifier, weight);
    }

    public EffectPredicateContext(WeightedContext<MobEffectInstance> weightedList) {
        this.effectInstances = weightedList;
    }

    public WeightedContext<MobEffectInstance> getEffects() {
        return effectInstances;
    }
    public void addEffect(Holder<MobEffect> effect, int duration, int amplifier, int weight) {
        effectInstances.add(new MobEffectInstance(effect, duration, amplifier), weight);
    }

    @Override
    public Codec<EffectPredicateContext> getCodec() {
        return CODEC;
    }
}
