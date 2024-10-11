package com.xiaohunao.heaven_destiny_moment.common.context.reward;

import com.mojang.serialization.Codec;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecExtra;
import com.xiaohunao.heaven_destiny_moment.common.context.WeightedContext;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

public class EffectRewardContext extends RewardContext {
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("effect");
    public static final Codec<EffectRewardContext> CODEC = WeightedContext.codec(CodecExtra.MOB_EFFECT_INSTANCE_CODEC)
            .fieldOf("effects")
            .xmap(EffectRewardContext::new, EffectRewardContext::getEffect)
            .codec();

    private WeightedContext<MobEffectInstance> effectInstances = WeightedContext.create();
    public EffectRewardContext(WeightedContext<MobEffectInstance> effectInstances) {
        this.effectInstances = effectInstances;
    }

    public WeightedContext<MobEffectInstance> getEffect() {
        return effectInstances;
    }
    public EffectRewardContext(MobEffect effect, int duration, int amplifier, int weight) {
        addEffect(effect, duration, amplifier, weight);
    }

    @Override
    public void createReward(MomentInstance momentInstance, Player player) {
        effectInstances.getRandomValue(momentInstance.getLevel().random).ifPresent(player::addEffect);
    }

    public void addEffect(MobEffect effect, int duration, int amplifier, int weight) {
        effectInstances.add(new MobEffectInstance(effect, duration, amplifier), weight);
    }

    @Override
    public Codec<? extends RewardContext> getCodec() {
        return CODEC;
    }
}
