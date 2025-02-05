package com.xiaohunao.heaven_destiny_moment.common.callback;

import com.mojang.serialization.Codec;

import java.io.Serializable;


public interface CallbackSerializable extends Serializable {
    Codec<CallbackSerializable> CODEC = CallbackMetadata.CODEC.xmap(CallbackMetadata::deserialize, CallbackMetadata::serialize);
}
