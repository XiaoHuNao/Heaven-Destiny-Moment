package com.xiaohunao.heaven_destiny_moment.common.context.amount;

import com.mojang.serialization.Codec;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecMap;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecProvider;


public abstract class AmountContext implements CodecProvider<AmountContext> {
    public final static CodecMap<AmountContext> CODEC = new CodecMap<>("Amount");

    public static void register() {
        CODEC.register(IntegerAmountContext.ID, IntegerAmountContext.CODEC);
        CODEC.register(RandomAmountContext.ID , RandomAmountContext.CODEC);
    }

    public abstract int getAmount();

    @Override
    public abstract Codec<? extends AmountContext> getCodec();
}
