package com.xiaohunao.heaven_destiny_moment.common.callback;

import com.xiaohunao.heaven_destiny_moment.common.context.reward.IReward;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.world.entity.player.Player;

@FunctionalInterface
public interface RewardCallBack {
    void createReward(IReward reward, MomentInstance<?> instance, Player player);
}
