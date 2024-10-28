package com.xiaohunao.heaven_destiny_moment.common.init;

import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.client.gui.bar.render.IBarRenderType;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentType;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.worldgen.biome.BiomeData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class MomentRegistries {
    public static final Registry<MomentType<?>> MOMENT_TYPE = new RegistryBuilder<>(Keys.MOMENT_TYPE).create();
    public static final Registry<IBarRenderType> BAR_RENDER_TYPE = new RegistryBuilder<>(Keys.BAR_RENDER_TYPE).create();



    public static final class Keys {
        public static final ResourceKey<Registry<Moment>> MOMENT = ResourceKey.createRegistryKey(HeavenDestinyMoment.asResource("moment"));
        public static final ResourceKey<Registry<MomentType<?>>> MOMENT_TYPE = ResourceKey.createRegistryKey(HeavenDestinyMoment.asResource("moment_type"));
        public static final ResourceKey<Registry<IBarRenderType>> BAR_RENDER_TYPE = ResourceKey.createRegistryKey(HeavenDestinyMoment.asResource("bar_render_type"));

    }

    public static final RegistrySetBuilder REGISTRY_SET_BUILDER = new RegistrySetBuilder()
            .add(Keys.MOMENT, ModMoments::bootstrap);


    public static void registerRegistries(NewRegistryEvent event) {
        event.register(MOMENT_TYPE);
        event.register(BAR_RENDER_TYPE);

    }

    public static void registerDataPackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(Keys.MOMENT, Moment.CODEC, Moment.CODEC);
    }
}
