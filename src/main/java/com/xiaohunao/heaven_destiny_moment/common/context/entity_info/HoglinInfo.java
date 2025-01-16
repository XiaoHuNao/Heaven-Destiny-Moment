package com.xiaohunao.heaven_destiny_moment.common.context.entity_info;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.IAmount;
import com.xiaohunao.heaven_destiny_moment.common.context.attachable.IAttachable;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMContextRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HoglinInfo extends EntityInfo{
    public static final MapCodec<HoglinInfo> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(EntityInfo::entityType),
            IAmount.CODEC.optionalFieldOf("amount").forGetter(EntityInfo::amount),
            Codec.INT.optionalFieldOf("weight").forGetter(EntityInfo::weight),
            IAttachable.CODEC.listOf().optionalFieldOf("attaches").forGetter(EntityInfo::attaches),
            Codec.unboundedMap(EquipmentSlot.CODEC,Codec.FLOAT).optionalFieldOf("canDropEquippable").forGetter(EntityInfo::canDropEquippable),
            IEntityInfo.CODEC.optionalFieldOf("vehicle").forGetter(EntityInfo::vehicle),
            Codec.BOOL.fieldOf("immuneZombification").forGetter(HoglinInfo::immuneZombification)
    ).apply(instance, HoglinInfo::new));

    private final boolean immuneZombification;

    public HoglinInfo(EntityType<?> entityType, Optional<IAmount> amount, Optional<Integer> weight, Optional<List<IAttachable>> attaches, Optional<Map<EquipmentSlot, Float>> canDropEquippable, Optional<IEntityInfo> vehicle, boolean immuneZombification) {
        super(entityType, amount, weight, attaches, canDropEquippable, vehicle);
        this.immuneZombification = immuneZombification;
    }

    @Override
    public List<Entity> spawn(Level level) {
        List<Entity> spawn = super.spawn(level);
        spawn.forEach(entity -> {
            if (entity instanceof Hoglin hoglin){
                hoglin.setImmuneToZombification(immuneZombification);
            }
        });
        return spawn;
    }

    @Override
    public MapCodec<? extends IEntityInfo> codec() {
        return HDMContextRegister.HOGLIN_INFO.get();
    }

    public boolean immuneZombification() {
        return immuneZombification;
    }

    public static class Builder extends EntityInfo.Builder {
        private boolean immuneZombification = false;

        public Builder(EntityType<?> entityType) {
            super(entityType);
        }

        public Builder immuneZombification(boolean immuneZombification) {
            this.immuneZombification = immuneZombification;
            return this;
        }

        @Override
        public HoglinInfo build() {
            return new HoglinInfo(entityType, Optional.ofNullable(amount), Optional.ofNullable(weight), Optional.ofNullable(attaches),
                    Optional.ofNullable(canDropEquippable), Optional.ofNullable(vehicle), immuneZombification);
        }
    }
}
