package com.xiaohunao.heaven_destiny_moment.common.callback.callback;

import com.xiaohunao.heaven_destiny_moment.common.callback.CallbackSerializable;
import com.xiaohunao.heaven_destiny_moment.compat.kubejs.KubeJSMoment;
import net.minecraft.world.entity.player.Player;

@FunctionalInterface
public interface RewardCallback extends CallbackSerializable {
    void execute(KubeJSMoment.KubeJSMomentInstance instance, Player player);
}
