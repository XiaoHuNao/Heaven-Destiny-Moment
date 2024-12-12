package com.xiaohunao.heaven_destiny_moment.common.context.amount;


import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMRegistries;

import java.util.function.Function;

public  interface IAmount {
    Codec<IAmount> CODEC = Codec.lazyInitialized(() -> HDMRegistries.Suppliers.AMOUNT_CODEC.get().byNameCodec()).dispatch(IAmount::codec, Function.identity());

    int getAmount();

    MapCodec<? extends IAmount> codec();
}
