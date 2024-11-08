package com.xiaohunao.heaven_destiny_moment.common.context.amount;


import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.xiaohunao.heaven_destiny_moment.common.init.MomentRegistries;

import java.util.function.Function;

public  interface IAmountContext {
    Codec<IAmountContext> CODEC = Codec.lazyInitialized(() -> MomentRegistries.Suppliers.AMOUNT_CODEC.get().byNameCodec()).dispatch(IAmountContext::codec, Function.identity());

    int getAmount();

    MapCodec<? extends IAmountContext> codec();
}
