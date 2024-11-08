package com.xiaohunao.heaven_destiny_moment.common.context.amount;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.init.ModContextRegister;

import java.util.Random;

public record RandomAmountContext(int min, int max) implements IAmountContext {
    public static final MapCodec<RandomAmountContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("min").forGetter(RandomAmountContext::min),
            Codec.INT.fieldOf("max").forGetter(RandomAmountContext::max)
    ).apply(instance, RandomAmountContext::new));

    private static final Random rand = new Random();

    @Override
    public int getAmount() {
        int newMax = Math.max(this.max, this.min);
        return Math.max(0, rand.nextInt(newMax - this.min + 1) + this.min);
    }
    @Override
    public MapCodec<? extends IAmountContext> codec() {
        return ModContextRegister.RANDOM_AMOUNT.get();
    }
}