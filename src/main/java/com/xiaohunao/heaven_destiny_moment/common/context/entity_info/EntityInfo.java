package com.xiaohunao.heaven_destiny_moment.common.context.entity_info;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.IAmount;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.IntegerAmount;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.RandomAmount;
import com.xiaohunao.heaven_destiny_moment.common.context.attachable.IAttachable;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMContextRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class EntityInfo implements IEntityInfo {
    public static final MapCodec<EntityInfo> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(EntityInfo::entityType),
            IAmount.CODEC.optionalFieldOf("amount").forGetter(EntityInfo::amount),
            Codec.INT.optionalFieldOf("weight").forGetter(EntityInfo::weight),
            IAttachable.CODEC.listOf().optionalFieldOf("attaches").forGetter(EntityInfo::attaches),
            Codec.unboundedMap(EquipmentSlot.CODEC,Codec.FLOAT).optionalFieldOf("canDropEquippable").forGetter(EntityInfo::canDropEquippable)
    ).apply(instance, EntityInfo::new));

    private final EntityType<?> entityType;
    private final Optional<IAmount> amount;
    private final Optional<Integer> weight;
    private final Optional<List<IAttachable>> attaches;
    private final Optional<Map<EquipmentSlot,Float>> canDropEquippable;

    public EntityInfo(EntityType<?> entityType, Optional<IAmount> amount, Optional<Integer> weight, Optional<List<IAttachable>> attaches, Optional<Map<EquipmentSlot, Float>> canDropEquippable) {
        this.entityType = entityType;
        this.amount = amount;
        this.weight = weight;
        this.attaches = attaches;
        this.canDropEquippable = canDropEquippable;
    }


    @Override
    public List<Entity> spawn(Level level) {
        List<Entity> arrayList = Lists.newArrayList();
        IAmount amount = amount().orElse(new IntegerAmount(1));
        for (int i = 0; i < amount.getAmount(); i++) {
            Entity entity = entityType.create(level);
            attaches.ifPresent(attaches -> {
                if (entity instanceof LivingEntity livingEntity) {
                    attaches.forEach(attachable -> attachable.attachToEntity(livingEntity));
                }
            });

            canDropEquippable.ifPresent(canDropEquippable ->{
                canDropEquippable.forEach((slot, chance) -> {
                    if (entity instanceof Mob mob) {
                        mob.setDropChance(slot, chance);
                    }
                });
            });

            arrayList.add(entity);
        }
        return arrayList;
    }

    @Override
    public MapCodec<? extends IEntityInfo> codec() {
        return HDMContextRegister.ENTITY_INFO.get();
    }

    public EntityType<?> entityType() {
        return entityType;
    }

    public Optional<IAmount> amount() {
        return amount;
    }

    public Optional<Integer> weight() {
        return weight;
    }

    public Optional<List<IAttachable>> attaches() {
        return attaches;
    }

    public Optional<Map<EquipmentSlot, Float>> canDropEquippable() {
        return canDropEquippable;
    }

    public static class Builder {
        protected EntityType<?> entityType;
        protected IAmount amount;
        protected List<IAttachable> attaches;
        protected Integer weight;
        protected Map<EquipmentSlot,Float> canDropEquippable;

        public Builder(EntityType<?> entityType) {
            this.entityType = (entityType);
        }

        public Builder amount(IAmount amount) {
            this.amount = amount;
            return this;
        }

        public Builder amount(int amount) {
            this.amount = new IntegerAmount(amount);
            return this;
        }

        public Builder amount(int min, int max) {
            this.amount = new RandomAmount(min, max);
            return this;
        }

        public Builder weight(int weight) {
            this.weight = weight;
            return this;
        }

        public Builder attachable(IAttachable... attachable) {
            if (attaches == null) {
                attaches = Lists.newArrayList();
            }
            attaches.addAll(List.of(attachable));
            return this;
        }

        public Builder canDropEquippable(EquipmentSlot slot,float chance) {
            if (this.canDropEquippable == null){
                this.canDropEquippable = Maps.newHashMap();
            }
            this.canDropEquippable.put(slot,chance);
            return this;
        }


        public IEntityInfo build() {
            return new EntityInfo(entityType, Optional.ofNullable(amount), Optional.ofNullable(weight), Optional.ofNullable(attaches), Optional.ofNullable(canDropEquippable));
        }
    }
}
