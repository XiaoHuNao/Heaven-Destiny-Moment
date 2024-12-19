package com.xiaohunao.heaven_destiny_moment.compat.kubejs.event;

import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentState;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.event.EventTargetType;
import dev.latvian.mods.kubejs.event.TargetedEventHandler;
import dev.latvian.mods.rhino.type.TypeInfo;

public interface HDMMomentKubeJSEvents {
    EventTargetType<MomentState> MOMENT_STATE = EventTargetType.create(MomentState.class).transformer(MomentState::toMomentState).describeType(TypeInfo.of(MomentState.class));

    EventGroup MOMENTS = EventGroup.of("MomentEvents");

    EventHandler Tick = MOMENTS.server("tick", () -> MomentTickEventJS.class);
    TargetedEventHandler<MomentState> STATE = MOMENTS.server("state", () -> MomentStateEventJS.class).hasResult().requiredTarget(MOMENT_STATE);
}
