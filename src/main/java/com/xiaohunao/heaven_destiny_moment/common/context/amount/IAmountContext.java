package com.xiaohunao.heaven_destiny_moment.common.context.amount;


import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMRegistries;

import java.util.function.Function;

public  interface IAmountContext {
    Codec<IAmountContext> CODEC = Codec.lazyInitialized(() -> HDMRegistries.Suppliers.AMOUNT_CODEC.get().byNameCodec()).dispatch(IAmountContext::codec, Function.identity());

    int getAmount();

    MapCodec<? extends IAmountContext> codec();
}
