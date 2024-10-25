package com.xiaohunao.heaven_destiny_moment.common.moment;

import com.mojang.serialization.Codec;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecMap;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecProvider;

public interface IMoment extends CodecProvider<IMoment> {
    Codec<Moment> DATA_CODEC = new CodecMap<>("moment");
//    Codec<Moment> CODEC = ModMoment.REGISTRY.byNameCodec();
}
