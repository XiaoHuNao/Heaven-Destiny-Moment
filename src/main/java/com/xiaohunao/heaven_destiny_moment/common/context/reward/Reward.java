package com.xiaohunao.heaven_destiny_moment.common.context.reward;

import com.xiaohunao.heaven_destiny_moment.common.callback.CallbackSerializable;
import com.xiaohunao.heaven_destiny_moment.common.callback.callback.RewardCallback;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public abstract class Reward implements IReward{
    protected RewardCallback rewardCallback;

    @Override
    public void createReward(MomentInstance<?> momentInstance, Player player) {
        Optional.ofNullable(rewardCallback).ifPresentOrElse(
                callback -> callback.createReward(momentInstance, player),
                () -> defaultRewards(momentInstance, player)
        );
    }


    public abstract void defaultRewards(MomentInstance<?> momentInstance, Player player);

    public CallbackSerializable getRewardCallback() {
        return rewardCallback;
    }
    public Reward rewardCallback(CallbackSerializable rewardCallback) {
        this.rewardCallback = (RewardCallback) rewardCallback;
        return this;
    }
}
