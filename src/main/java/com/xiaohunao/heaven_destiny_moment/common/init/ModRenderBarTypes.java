package com.xiaohunao.heaven_destiny_moment.common.init;

import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.client.gui.bar.render.DefaultBarRenderType;
import com.xiaohunao.heaven_destiny_moment.client.gui.bar.render.IBarRenderType;
import com.xiaohunao.heaven_destiny_moment.client.gui.bar.render.TerrariaBarRenderType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModRenderBarTypes {
    public static final ResourceKey<Registry<IBarRenderType>> RESOURCE_KEY = ResourceKey.createRegistryKey(HeavenDestinyMoment.asResource("bar_render_type"));
    public static final DeferredRegister<IBarRenderType> BAR_RENDER_TYPE = DeferredRegister.create(RESOURCE_KEY, HeavenDestinyMoment.MODID);
    public static final Supplier<IForgeRegistry<IBarRenderType>> REGISTRY_SUPPLIER = BAR_RENDER_TYPE.makeRegistry(RegistryBuilder::new);

    public static final RegistryObject<IBarRenderType> TERRA = BAR_RENDER_TYPE.register("terra", TerrariaBarRenderType::new);
    public static final RegistryObject<IBarRenderType> DEFAULT = BAR_RENDER_TYPE.register("default", DefaultBarRenderType::new);
}
