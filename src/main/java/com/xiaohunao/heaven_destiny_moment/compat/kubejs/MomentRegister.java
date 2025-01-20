package com.xiaohunao.heaven_destiny_moment.compat.kubejs;

import com.mojang.serialization.MapCodec;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMRegistries;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentType;
import dev.latvian.mods.kubejs.KubeJS;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MomentRegister {
    public static final DeferredRegister<MapCodec<? extends Moment<?>>> MOMENT_CODEC = DeferredRegister.create(HDMRegistries.Keys.MOMENT_CODEC, KubeJS.MOD_ID);
    public static final DeferredRegister<MomentType<?>> MOMENT_TYPE = DeferredRegister.create(HDMRegistries.MOMENT_TYPE,KubeJS.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends Moment<?>>, MapCodec<KubeJSMoment>> KUBEJS_MOMENT = MOMENT_CODEC.register("kubejs", () -> KubeJSMoment.CODEC);

    public static final DeferredHolder<MomentType<?>, MomentType<KubeJSMoment.KubeJSMomentInstance>> KUBEJS = MOMENT_TYPE.register("kubejs",
            () -> new MomentType.Builder<>(KubeJSMoment.KubeJSMomentInstance::new).build());
}
