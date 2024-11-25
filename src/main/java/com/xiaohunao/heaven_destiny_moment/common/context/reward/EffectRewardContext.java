package com.xiaohunao.heaven_destiny_moment.common.context.reward;

import com.mojang.serialization.MapCodec;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.context.WeightedContext;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMContextRegister;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

public record EffectRewardContext(WeightedContext<MobEffectInstance> effectInstances) implements IRewardContext {
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("effect");
    public static final MapCodec<EffectRewardContext> CODEC = MapCodec.assumeMapUnsafe(WeightedContext.codec(MobEffectInstance.CODEC)
            .fieldOf("effects")
            .xmap(EffectRewardContext::new, EffectRewardContext::effectInstances)
            .codec());

    @Override
    public void createReward(MomentInstance momentInstance, Player player) {
        effectInstances.getRandomValue(momentInstance.getLevel().random).ifPresent(player::addEffect);
    }

    @Override
    public MapCodec<? extends IRewardContext> codec() {
        return HDMContextRegister.EFFECT_REWARD.get();
    }
}
