package com.xiaohunao.heaven_destiny_moment.common.context.amount;


import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMContextRegister;


public record IntegerAmountContext(int amount) implements IAmountContext {
    public static final MapCodec<IntegerAmountContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("amount").forGetter(IntegerAmountContext::amount)
    ).apply(instance, IntegerAmountContext::new));

    @Override
    public int getAmount() {
        return amount;
    }
    @Override
    public MapCodec<? extends IAmountContext> codec() {
        return HDMContextRegister.INTEGER_AMOUNT.get();
    }
}
