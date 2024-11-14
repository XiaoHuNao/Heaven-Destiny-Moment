package com.xiaohunao.heaven_destiny_moment.common.context.entity_info;

import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.IAmountContext;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.IntegerAmountContext;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.RandomAmountContext;
import com.xiaohunao.heaven_destiny_moment.common.context.attachable.IAttachable;
import com.xiaohunao.heaven_destiny_moment.common.init.ModContextRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

import java.util.List;
import java.util.Optional;

public record EntityInfoContext(EntityType<?> entityType, IAmountContext amount,Optional<List<IAttachable>> attaches) implements IEntityInfoContext {
    public static final MapCodec<EntityInfoContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(EntityInfoContext::getEntityType),
            IAmountContext.CODEC.fieldOf("amount").forGetter(EntityInfoContext::getAmount),
            IAttachable.CODEC.listOf().optionalFieldOf("attaches").forGetter(EntityInfoContext::attaches)
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
        private Optional<List<IAttachable>> attaches;

        public Builder(EntityType<?> entityType, int min, int max) {
            this.entityType = entityType;
            this.amount = new RandomAmountContext(min, max);
        }

        public Builder(EntityType<?> entityType, int amount) {
            this.entityType = entityType;
            this.amount = new IntegerAmountContext(amount);
        }
        public Builder addAttachable(IAttachable... attachable){
            if (attaches.isEmpty()){
                attaches = Optional.of(Lists.newArrayList());
            }
            attaches.get().addAll(List.of(attachable));
            return this;
        }

        public EntityInfoContext build() {
            return new EntityInfoContext(entityType, amount,attaches);
        }
    }
}
