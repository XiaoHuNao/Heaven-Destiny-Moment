package com.xiaohunao.heaven_destiny_moment.common.codec;

import com.mojang.serialization.Codec;

public interface CodecProvider<T> {
    Codec<? extends T> getCodec();
}