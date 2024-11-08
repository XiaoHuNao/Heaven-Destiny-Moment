package com.xiaohunao.heaven_destiny_moment.common.init;

import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.client.gui.bar.render.DefaultBarRenderType;
import com.xiaohunao.heaven_destiny_moment.client.gui.bar.render.IBarRenderType;
import com.xiaohunao.heaven_destiny_moment.client.gui.bar.render.TerrariaBarRenderType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBarRenderTypes {
    public static final DeferredRegister<IBarRenderType> BAR_RENDER_TYPE = DeferredRegister.create(MomentRegistries.BAR_RENDER_TYPE, HeavenDestinyMoment.MODID);

    public static final DeferredHolder<IBarRenderType, TerrariaBarRenderType> TERRA = BAR_RENDER_TYPE.register("terra", TerrariaBarRenderType::new);
    public static final DeferredHolder<IBarRenderType, DefaultBarRenderType> DEFAULT = BAR_RENDER_TYPE.register("default", DefaultBarRenderType::new);

}
