package com.xiaohunao.heaven_destiny_moment.common.callback;

import com.google.common.collect.Maps;
import com.xiaohunao.heaven_destiny_moment.common.context.reward.IReward;

import java.util.Map;
import java.util.UUID;

public class MomentCallbackManager {
    private final static Map<UUID,Map<String, MomentInstanceCallback<?, ?>>> momentInstanceCallbacks = Maps.newHashMap();
    private final static Map<UUID,RewardCallBack<? extends IReward>> rewardCallbacks = Maps.newHashMap();

    public static void registerMomentInstanceCallback(UUID uuid, String methodName, MomentInstanceCallback<?, ?> callback) {
        Map<String, MomentInstanceCallback<?, ?>> callbacks = momentInstanceCallbacks.getOrDefault(uuid, Maps.newHashMap());
        callbacks.put(methodName, callback);
        momentInstanceCallbacks.put(uuid, callbacks);
    }

    public static Map<String, MomentInstanceCallback<?, ?>> getMomentInstanceCallback(UUID uuid) {
        return momentInstanceCallbacks.getOrDefault(uuid, Maps.newHashMap());
    }

    public static void registerRewardCallback(UUID methodNameUid, RewardCallBack<? extends IReward> callback) {
        rewardCallbacks.put(methodNameUid, callback);
    }

    public static RewardCallBack getRewardCallback(UUID methodNameUid) {
       return rewardCallbacks.get(methodNameUid);
    }
}
