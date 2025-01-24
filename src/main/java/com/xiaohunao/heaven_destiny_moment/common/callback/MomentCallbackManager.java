package com.xiaohunao.heaven_destiny_moment.common.callback;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public class MomentCallbackManager {
    private final static Map<UUID, Map<String, MomentInstanceCallback<?, ?>>> momentInstanceCallbacks = Maps.newHashMap();
    private final static Map<UUID, Map<String, RewardCallBack>> rewardCallbacks = Maps.newHashMap();

    public static void registerMomentInstanceCallback(UUID uuid, String methodName, MomentInstanceCallback<?, ?> callback) {
        Map<String, MomentInstanceCallback<?, ?>> callbacks = momentInstanceCallbacks.getOrDefault(uuid, Maps.newHashMap());
        callbacks.put(methodName, callback);
        momentInstanceCallbacks.put(uuid, callbacks);
    }

    public static Map<String, MomentInstanceCallback<?, ?>> getMomentInstanceCallback(UUID uuid) {
        return momentInstanceCallbacks.computeIfAbsent(uuid, id -> Maps.newHashMap());
    }

    public static @Nullable RewardCallBack getRewardCallback(UUID momentId, String identifier) {
        Map<String, RewardCallBack> map = rewardCallbacks.get(momentId);
        return map == null ? null : map.get(identifier);
    }
}
