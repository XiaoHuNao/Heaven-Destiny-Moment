package com.xiaohunao.heaven_destiny_moment.common.moment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecMap;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecProvider;
import com.xiaohunao.heaven_destiny_moment.common.init.MomentRegistries;
import com.xiaohunao.heaven_destiny_moment.common.moment.area.Area;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public interface IMoment {
    Codec<Moment> CODEC = Codec.lazyInitialized(() -> MomentRegistries.Suppliers.MOMENT_CODEC.get().byNameCodec()).dispatch(Moment::codec, Function.identity());

    MapCodec<? extends Moment> codec();

    ResourceLocation getBarRenderType();

    void tick(MomentInstance momentInstance);
}
