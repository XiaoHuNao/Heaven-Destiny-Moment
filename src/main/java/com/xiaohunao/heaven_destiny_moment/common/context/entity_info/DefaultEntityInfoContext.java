package com.xiaohunao.heaven_destiny_moment.common.context.entity_info;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.AmountContext;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.IntegerAmountContext;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.RandomAmountContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public class DefaultEntityInfoContext extends EntityInfoContext{
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("entity_info");
    public static final Codec<DefaultEntityInfoContext> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(DefaultEntityInfoContext::getEntityType),
            AmountContext.CODEC.fieldOf("amount_module").forGetter(DefaultEntityInfoContext::getAmount)
    ).apply(instance, DefaultEntityInfoContext::new));


    protected EntityType<?> entityType;
    protected AmountContext amount;
    public DefaultEntityInfoContext(EntityType<?> entityType) {
        this(entityType,1);
    }

    public DefaultEntityInfoContext(EntityType<?> entityType, int amount) {
        this(entityType,new IntegerAmountContext(amount));
    }

    public DefaultEntityInfoContext(EntityType<?> entityType, int min, int max) {
        this(entityType,new RandomAmountContext(min, max));
    }

    public DefaultEntityInfoContext(EntityType<?> entityType, AmountContext amount) {
        this.entityType = entityType;
        this.amount = amount;
    }

    @Override
    public EntityType<?> getEntityType() {
        return entityType;
    }

    @Override
    public int getSpawnAmount() {
        return amount.getAmount();
    }
    @Override
    public AmountContext getAmount() {
        return amount;
    }

    @Override
    public Codec<? extends EntityInfoContext> getCodec() {
        return CODEC;
    }
}
