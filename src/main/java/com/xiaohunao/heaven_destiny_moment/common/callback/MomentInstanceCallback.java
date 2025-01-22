package com.xiaohunao.heaven_destiny_moment.common.callback;

import com.xiaohunao.heaven_destiny_moment.compat.kubejs.KubeJSMoment;

@FunctionalInterface
public interface MomentInstanceCallback<T, R> {
    R execute(KubeJSMoment.KubeJSMomentInstance instance, T param);
}
