package com.xiaohunao.heaven_destiny_moment.common.context.entity_info;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.IAmountContext;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.IntegerAmountContext;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.RandomAmountContext;
import com.xiaohunao.heaven_destiny_moment.common.init.ModContextRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.Map;

public record EntityInfoContext(EntityType<?> entityType, IAmountContext amount) implements IEntityInfoContext {
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("entity_info");
    public static final MapCodec<EntityInfoContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(EntityInfoContext::getEntityType),
            IAmountContext.CODEC.fieldOf("amount").forGetter(EntityInfoContext::getAmount)
    ).apply(instance, EntityInfoContext::new));


    @Override
    public EntityType<?> getEntityType() {
        return entityType;
    }

    @Override
    public int getSpawnAmount() {
        return amount.getAmount();
    }

    @Override
    public IAmountContext getAmount() {
        return amount;
    }

    @Override
    public MapCodec<? extends IEntityInfoContext> codec() {
        return ModContextRegister.ENTITY_INFO.get();
    }

    public static class Builder {
        private final EntityType<?> entityType;
        private final IAmountContext amount;
        private final Map<AttachmentType<?>, JsonElement> attachments = Maps.newHashMap();

        private Builder(EntityType<?> entityType,int min,int max) {
            this.entityType = entityType;
            this.amount = new RandomAmountContext(min, max);
        }
        private Builder(EntityType<?> entityType,int amount) {
            this.entityType = entityType;
            this.amount = new IntegerAmountContext(amount);
        }
        public EntityInfoContext build() {
            return new EntityInfoContext(entityType, amount);
        }

    }
}
