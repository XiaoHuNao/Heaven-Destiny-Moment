package com.xiaohunao.heaven_destiny_moment.common.context.reward;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMRegistries;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.world.entity.player.Player;

import java.util.function.Function;

public interface IReward {
    Codec<IReward> CODEC = Codec.lazyInitialized(() -> HDMRegistries.Suppliers.REWARD_CODEC.get().byNameCodec()).dispatch(IReward::codec, Function.identity());

    MapCodec<? extends IReward> codec();

    void createReward(MomentInstance<?> momentInstance, Player player);
}
