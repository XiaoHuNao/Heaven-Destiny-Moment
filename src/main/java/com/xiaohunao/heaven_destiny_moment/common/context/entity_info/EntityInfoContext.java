package com.xiaohunao.heaven_destiny_moment.common.context.entity_info;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.IAmountContext;
import com.xiaohunao.heaven_destiny_moment.common.context.attachable.IAttachable;
import com.xiaohunao.heaven_destiny_moment.common.init.ModContextRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public record EntityInfoContext(EntityType<?> entityType, Optional<IAmountContext> amount, Optional<Integer> weight, Optional<List<IAttachable>> attaches) implements IEntityInfoContext {
    public static final MapCodec<EntityInfoContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(EntityInfoContext::entityType),
            IAmountContext.CODEC.optionalFieldOf("amount").forGetter(EntityInfoContext::amount),
            Codec.INT.optionalFieldOf("weight").forGetter(EntityInfoContext::weight),
            IAttachable.CODEC.listOf().optionalFieldOf("attaches").forGetter(EntityInfoContext::attaches)
    ).apply(instance, EntityInfoContext::new));




//    @Override
//    public Entity crateEntity(Level level) {
//        Entity entity = entityType.create(level);
//        if (entity instanceof LivingEntity livingEntity) {
//            attaches.ifPresent(attaches -> {
//                for (IAttachable attach : attaches) {
//                    attach.attachToEntity(livingEntity);
//                }
//            });
//        }
//        return entity;
//    }


    @Override
    public MapCodec<? extends IEntityInfoContext> codec() {
        return ModContextRegister.ENTITY_INFO.get();
    }


    public static class Builder {
        protected EntityType<?> entityType;
        protected Optional<IAmountContext> amount;
        protected Optional<List<IAttachable>> attaches;
        protected Optional<Integer> weight;

        public Builder(EntityType<?> entityType) {
            this.entityType = (entityType);
        }

        public Builder amount(IAmountContext amount){
            this.amount = Optional.of(amount);
            return this;
        }

        public Builder weight(int weight){
            this.weight = Optional.of(weight);
            return this;
        }

        public Builder attachable(IAttachable... attachable) {
            if (attaches.isEmpty()) {
                attaches = Optional.of(Lists.newArrayList());
            }
            attaches.get().addAll(List.of(attachable));
            return this;
        }

        public IEntityInfoContext build() {
            return new EntityInfoContext(entityType, amount, weight, attaches);
        }
    }
}
