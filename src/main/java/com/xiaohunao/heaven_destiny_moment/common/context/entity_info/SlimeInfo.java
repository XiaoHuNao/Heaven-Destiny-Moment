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
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;


public class SlimeInfo extends EntityInfo{
    public static final MapCodec<SlimeInfo> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(EntityInfo::entityType),
            IAmount.CODEC.optionalFieldOf("amount").forGetter(EntityInfo::amount),
            Codec.INT.optionalFieldOf("weight").forGetter(EntityInfo::weight),
            IAttachable.CODEC.listOf().optionalFieldOf("attaches").forGetter(EntityInfo::attaches),
            Codec.INT.fieldOf("size").forGetter(SlimeInfo::size)
    ).apply(instance, SlimeInfo::new));
    private final Integer size;

    public SlimeInfo(EntityType<?> entityType, Optional<IAmount> amount, Optional<Integer> weight, Optional<List<IAttachable>> attaches, Integer size) {
        super(entityType, amount, weight, attaches);
        this.size = size;
    }

    @Override
    public List<Entity> spawn(Level level) {
        List<Entity> spawn = super.spawn(level);
        spawn.forEach(entity -> {
            if (entity instanceof Slime slime){
                slime.setSize(size,false);
            }
        });
        return spawn;
    }

    @Override
    public MapCodec<? extends IEntityInfo> codec() {
        return HDMContextRegister.SLIME_INFO.get();
    }

    public Integer size() {
        return size;
    }

    public static class Builder extends EntityInfo.Builder {
        private Integer size;

        public Builder(EntityType<?> entityType) {
            super(entityType);
        }

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        @Override
        public IEntityInfo build() {
            return new SlimeInfo(entityType, amount, weight, attaches, size);
        }
    }
}
