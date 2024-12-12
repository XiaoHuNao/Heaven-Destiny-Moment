package com.xiaohunao.heaven_destiny_moment.common.context.entity_info;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMContextRegister;
import net.minecraft.world.entity.EntityType;


public record SlimeInfo(EntityInfo entityInfo , Integer size) implements IEntityInfo {
    public static final MapCodec<SlimeInfo> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            EntityInfo.CODEC.fieldOf("entity_info").forGetter(SlimeInfo::entityInfo),
            Codec.INT.fieldOf("size").forGetter(SlimeInfo::size)
    ).apply(instance, SlimeInfo::new));

//    @Override
//    public Entity crateEntity(Level level) {
//        Entity entity = super.crateEntity(level);
//
//        if (entity instanceof Slime slime && size != null) {
//            slime.setSize(size, true);
//        }
//        return entity;
//    }


    @Override
    public MapCodec<? extends IEntityInfo> codec() {
        return HDMContextRegister.SLIME_INFO.get();
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
            return new SlimeInfo(new EntityInfo(entityType, amount, weight, attaches), size);
        }
    }
}
