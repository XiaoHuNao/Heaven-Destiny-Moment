package com.xiaohunao.heaven_destiny_moment.common.context.amount;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import net.minecraft.resources.ResourceLocation;

import java.util.Random;

public class RandomAmountContext extends AmountContext {
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("random");
    public static final Codec<RandomAmountContext> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("min").forGetter(RandomAmountContext::getMin),
            Codec.INT.fieldOf("max").forGetter(RandomAmountContext::getMax)
    ).apply(instance, RandomAmountContext::new));

    private final Random rand = new Random();
    protected int min;
    protected int max;

    public RandomAmountContext(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public int getAmount() {
        this.max = Math.max(this.max, this.min);
        return Math.max(0, this.rand.nextInt(this.max - this.min + 1) + this.min);
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    @Override
    public Codec<? extends AmountContext> getCodec() {
        return CODEC;
    }
}