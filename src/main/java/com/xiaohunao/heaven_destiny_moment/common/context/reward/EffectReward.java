package com.xiaohunao.heaven_destiny_moment.common.context.reward;

import com.mojang.serialization.MapCodec;
import com.xiaohunao.heaven_destiny_moment.common.context.Weighted;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMContextRegister;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

public record EffectReward(Weighted<MobEffectInstance> effectInstances) implements IReward {
    public static final MapCodec<EffectReward> CODEC = MapCodec.assumeMapUnsafe(Weighted.codec(MobEffectInstance.CODEC)
            .fieldOf("effects")
            .xmap(EffectReward::new, EffectReward::effectInstances)
            .codec());

    @Override
    public void createReward(MomentInstance<?> momentInstance, Player player) {
        effectInstances.getRandomWeighted().forEach(player::addEffect);
    }

    @Override
    public MapCodec<? extends IReward> codec() {
        return HDMContextRegister.EFFECT_REWARD.get();
    }

    public static class Builder {
        private final Weighted.Builder<MobEffectInstance> builder = new Weighted.Builder<>();

        public EffectReward build() {
            return new EffectReward(builder.build());
        }
        public Builder randomType(Weighted.RandomType randomType){
            builder.randomType(randomType);
            return this;
        }

        public Builder addEffect(MobEffectInstance effectInstance, int weight) {
            builder.add(effectInstance, weight);
            return this;
        }

        public Builder addEffect(MobEffectInstance effectInstance) {
            builder.add(effectInstance, 1);
            return this;
        }
    }
}
