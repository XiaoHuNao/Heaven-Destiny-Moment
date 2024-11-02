package com.xiaohunao.heaven_destiny_moment.common.init;

import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.client.gui.bar.render.IBarRenderType;
import com.xiaohunao.heaven_destiny_moment.common.data.ServerMomentLoader;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentType;
import com.xiaohunao.heaven_destiny_moment.common.moment.area.Area;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

@EventBusSubscriber(modid = HeavenDestinyMoment.MODID, bus = EventBusSubscriber.Bus.GAME)
public class MomentRegistries {
    public static final Registry<MomentType<?>> MOMENT_TYPE = new RegistryBuilder<>(Keys.MOMENT_TYPE).create();
    public static final Registry<IBarRenderType> BAR_RENDER_TYPE = new RegistryBuilder<>(Keys.BAR_RENDER_TYPE).create();
    public static final Registry<Area<?>> AREA = new RegistryBuilder<>(Keys.COVERAGE).create();

    public static ServerMomentLoader LOADER;

    public static final class Keys {
        public static final ResourceKey<Registry<Moment>> MOMENT = ResourceKey.createRegistryKey(HeavenDestinyMoment.asResource("moment"));
        public static final ResourceKey<Registry<MomentType<?>>> MOMENT_TYPE = ResourceKey.createRegistryKey(HeavenDestinyMoment.asResource("moment_type"));
        public static final ResourceKey<Registry<IBarRenderType>> BAR_RENDER_TYPE = ResourceKey.createRegistryKey(HeavenDestinyMoment.asResource("bar_render_type"));
        public static final ResourceKey<Registry<Area<?>>> COVERAGE = ResourceKey.createRegistryKey(HeavenDestinyMoment.asResource("area"));
    }

    public static final RegistrySetBuilder REGISTRY_SET_BUILDER = new RegistrySetBuilder()
            .add(Keys.MOMENT, ModMoments::bootstrap);


    public static void registerRegistries(NewRegistryEvent event) {
        event.register(MOMENT_TYPE);
        event.register(BAR_RENDER_TYPE);
        event.register(AREA);
    }


//    public static void registerDataPackRegistries(DataPackRegistryEvent.NewRegistry event) {
//        event.dataPackRegistry(Keys.MOMENT, Moment.CODEC, Moment.CODEC);
//    }

    @SubscribeEvent
    public static void addReloadListener(AddReloadListenerEvent event) {
        LOADER = new ServerMomentLoader(event.getRegistryAccess());
        event.addListener(LOADER);
    }
}
