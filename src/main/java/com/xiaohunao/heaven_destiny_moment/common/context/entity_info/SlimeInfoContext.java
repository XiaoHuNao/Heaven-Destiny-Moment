package com.xiaohunao.heaven_destiny_moment.common.context.entity_info;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.init.ModContextRegister;
import net.minecraft.world.entity.EntityType;


public record SlimeInfoContext(EntityInfoContext entityInfo , Integer size) implements IEntityInfoContext{
    public static final MapCodec<SlimeInfoContext> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            EntityInfoContext.CODEC.fieldOf("entity_info").forGetter(SlimeInfoContext::entityInfo),
            Codec.INT.fieldOf("size").forGetter(SlimeInfoContext::size)
    ).apply(instance, SlimeInfoContext::new));

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
    public MapCodec<? extends IEntityInfoContext> codec() {
        return ModContextRegister.SLIME_INFO.get();
    }

    public static class Builder extends EntityInfoContext.Builder {
        private Integer size;

        public Builder(EntityType<?> entityType) {
            super(entityType);
        }

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        @Override
        public IEntityInfoContext build() {
            return new SlimeInfoContext(new EntityInfoContext(entityType, amount, weight, attaches), size);
        }
    }
}
