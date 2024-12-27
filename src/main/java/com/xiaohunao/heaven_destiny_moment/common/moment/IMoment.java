package com.xiaohunao.heaven_destiny_moment.common.moment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.xiaohunao.heaven_destiny_moment.client.gui.bar.render.IBarRenderType;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.function.Function;

public interface IMoment {
    Codec<Moment<?>> CODEC = Codec.lazyInitialized(() -> HDMRegistries.Suppliers.MOMENT_CODEC.get().byNameCodec()).dispatch(Moment::codec, Function.identity());

    MapCodec<? extends Moment<?>> codec();

    Optional<IBarRenderType> barRenderType();
}
