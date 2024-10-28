package com.xiaohunao.heaven_destiny_moment.common.moment;

import com.mojang.serialization.Codec;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecMap;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecProvider;
import net.minecraft.resources.ResourceLocation;

public interface IMoment extends CodecProvider<IMoment> {
    Codec<IMoment> DATA_CODEC = new CodecMap<>("moment");

    ResourceLocation getBarRenderType();

    void tick(MomentInstance momentInstance);
//    Codec<Moment> CODEC = ModMoment.REGISTRY.byNameCodec();
}
