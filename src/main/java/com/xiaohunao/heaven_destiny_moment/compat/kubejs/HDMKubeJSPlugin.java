package com.xiaohunao.heaven_destiny_moment.compat.kubejs;

import com.xiaohunao.heaven_destiny_moment.common.init.HDMRegistries;
import com.xiaohunao.heaven_destiny_moment.common.moment.IMoment;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentState;
import com.xiaohunao.heaven_destiny_moment.compat.kubejs.builder.CustomMomentKubeJSBuilder;
import com.xiaohunao.heaven_destiny_moment.compat.kubejs.builder.MomentKubeJSBuilder;
import com.xiaohunao.heaven_destiny_moment.compat.kubejs.builder.MomentTypeKubeJSBuilder;
import com.xiaohunao.heaven_destiny_moment.compat.kubejs.builder.RaidMomentKubeJSBuilder;
import com.xiaohunao.heaven_destiny_moment.compat.kubejs.event.HDMMomentKubeJSEvents;
import com.xiaohunao.heaven_destiny_moment.compat.kubejs.event.subscriber.KubeJSMomentEventSubscriber;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import dev.latvian.mods.kubejs.registry.ServerRegistryRegistry;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import dev.latvian.mods.kubejs.script.TypeWrapperRegistry;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.neoforged.neoforge.common.NeoForge;

public class HDMKubeJSPlugin implements KubeJSPlugin {
    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(HDMMomentKubeJSEvents.MOMENTS);
    }

    @Override
    public void registerBindings(BindingRegistry registry) {
        registry.add("MomentState", MomentState.class);
        registry.add("Moment", Moment.class);
    }

    @Override
    public void registerTypeWrappers(TypeWrapperRegistry registry) {
        registry.register(MomentState.class,MomentState::wrap);
    }

    @Override
    public void init() {
        NeoForge.EVENT_BUS.register(KubeJSMomentEventSubscriber.class);
    }

    @Override
    public void registerBuilderTypes(BuilderTypeRegistry registry) {
        registry.addDefault(HDMRegistries.Keys.MOMENT, MomentKubeJSBuilder.class, MomentKubeJSBuilder::new);

        registry.of(HDMRegistries.Keys.MOMENT, reg -> {
            reg.add("raid", RaidMomentKubeJSBuilder.class,RaidMomentKubeJSBuilder::new);
            reg.add("custom", CustomMomentKubeJSBuilder.class,CustomMomentKubeJSBuilder::new);
        });

        registry.addDefault(HDMRegistries.Keys.MOMENT_TYPE, MomentTypeKubeJSBuilder.class,MomentTypeKubeJSBuilder::new);
    }


    @Override
    public void registerServerRegistries(ServerRegistryRegistry registry) {
        registry.register(HDMRegistries.Keys.MOMENT, IMoment.CODEC, TypeInfo.of(Moment.class));
    }
}
