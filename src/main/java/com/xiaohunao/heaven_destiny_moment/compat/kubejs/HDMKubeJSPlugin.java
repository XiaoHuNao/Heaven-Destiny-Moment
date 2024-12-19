package com.xiaohunao.heaven_destiny_moment.compat.kubejs;

import com.xiaohunao.heaven_destiny_moment.compat.kubejs.event.HDMMomentKubeJSEvents;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;

public class HDMKubeJSPlugin implements KubeJSPlugin {
    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(HDMMomentKubeJSEvents.MOMENTS);
    }
}
