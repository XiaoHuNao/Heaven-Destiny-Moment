package com.xiaohunao.heaven_destiny_moment.common.context.entity_info;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.IAmount;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.IntegerAmount;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.RandomAmount;
import com.xiaohunao.heaven_destiny_moment.common.context.attachable.IAttachable;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMContextRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

import java.util.List;
import java.util.Optional;

public record EntityInfo(EntityType<?> entityType, Optional<IAmount> amount, Optional<Integer> weight, Optional<List<IAttachable>> attaches) implements IEntityInfo {
    public static final MapCodec<EntityInfo> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(EntityInfo::entityType),
            IAmount.CODEC.optionalFieldOf("amount").forGetter(EntityInfo::amount),
            Codec.INT.optionalFieldOf("weight").forGetter(EntityInfo::weight),
            IAttachable.CODEC.listOf().optionalFieldOf("attaches").forGetter(EntityInfo::attaches)
    ).apply(instance, EntityInfo::new));




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
    public MapCodec<? extends IEntityInfo> codec() {
        return HDMContextRegister.ENTITY_INFO.get();
    }


    public static class Builder {
        protected EntityType<?> entityType;
        protected Optional<IAmount> amount;
        protected Optional<List<IAttachable>> attaches;
        protected Optional<Integer> weight;

        public Builder(EntityType<?> entityType) {
            this.entityType = (entityType);
        }

        public Builder amount(IAmount amount){
            this.amount = Optional.of(amount);
            return this;
        }

        public Builder amount(int amount){
            this.amount = Optional.of(new IntegerAmount(amount));
            return this;
        }

        public Builder amount(int min,int max){
            this.amount = Optional.of(new RandomAmount(min,max));
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

        public IEntityInfo build() {
            return new EntityInfo(entityType, amount, weight, attaches);
        }
    }
}
