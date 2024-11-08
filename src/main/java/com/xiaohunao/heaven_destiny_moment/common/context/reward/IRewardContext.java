package com.xiaohunao.heaven_destiny_moment.common.context.reward;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.xiaohunao.heaven_destiny_moment.common.init.MomentRegistries;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.world.entity.player.Player;

import java.util.function.Function;

public interface IRewardContext {
    Codec<IRewardContext> CODEC = Codec.lazyInitialized(() -> MomentRegistries.Suppliers.REWARD_CODEC.get().byNameCodec()).dispatch(IRewardContext::codec, Function.identity());

    MapCodec<? extends IRewardContext> codec();

    void createReward(MomentInstance momentInstance, Player player);
}
