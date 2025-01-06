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
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PigLinInfo extends EntityInfo{
    public static final MapCodec<PigLinInfo> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(EntityInfo::entityType),
            IAmount.CODEC.optionalFieldOf("amount").forGetter(EntityInfo::amount),
            Codec.INT.optionalFieldOf("weight").forGetter(EntityInfo::weight),
            IAttachable.CODEC.listOf().optionalFieldOf("attaches").forGetter(EntityInfo::attaches),
            Codec.unboundedMap(EquipmentSlot.CODEC,Codec.FLOAT).optionalFieldOf("canDropEquippable").forGetter(EntityInfo::canDropEquippable),
            Codec.BOOL.fieldOf("immuneZombification").forGetter(PigLinInfo::immuneZombification)
    ).apply(instance, PigLinInfo::new));

    private final boolean immuneZombification;

    public PigLinInfo(EntityType<?> entityType, Optional<IAmount> amount, Optional<Integer> weight, Optional<List<IAttachable>> attaches, Optional<Map<EquipmentSlot, Float>> canDropEquippable, boolean immuneZombification) {
        super(entityType, amount, weight, attaches, canDropEquippable);
        this.immuneZombification = immuneZombification;
    }

    @Override
    public List<Entity> spawn(Level level) {
        List<Entity> spawn = super.spawn(level);
        spawn.forEach(entity -> {
            if (entity instanceof AbstractPiglin piglin){
                piglin.setImmuneToZombification(immuneZombification);
            }
        });
        return spawn;
    }

    @Override
    public MapCodec<? extends IEntityInfo> codec() {
        return HDMContextRegister.PIGLIN_INFO.get();
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
        public PigLinInfo build() {
            return new PigLinInfo(entityType, Optional.ofNullable(amount), Optional.ofNullable(weight), Optional.ofNullable(attaches), Optional.ofNullable(canDropEquippable), immuneZombification);
        }
    }
}
