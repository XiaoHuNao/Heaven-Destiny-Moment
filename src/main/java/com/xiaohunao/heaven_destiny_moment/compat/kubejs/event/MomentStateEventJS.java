package com.xiaohunao.heaven_destiny_moment.compat.kubejs.event;

import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import dev.latvian.mods.kubejs.event.KubeEvent;

public record MomentStateEventJS(MomentInstance<?> momentInstance) implements KubeEvent {

}
