package com.xiaohunao.heaven_destiny_moment.common.context.amount;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import net.minecraft.resources.ResourceLocation;


public class IntegerAmountContext extends AmountContext {
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("integer");
    public static final Codec<IntegerAmountContext> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("amount").forGetter(IntegerAmountContext::getAmount)
    ).apply(instance, IntegerAmountContext::new));
    protected int amount;

    public IntegerAmountContext(int amount) {
        this.amount = amount;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public Codec<? extends AmountContext> getCodec() {
        return CODEC;
    }
}
