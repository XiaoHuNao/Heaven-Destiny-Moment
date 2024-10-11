package com.xiaohunao.heaven_destiny_moment.common.context.entity_info;

import com.mojang.serialization.Codec;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecMap;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecProvider;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.AmountContext;
import net.minecraft.world.entity.EntityType;

public abstract class EntityInfoContext implements CodecProvider<EntityInfoContext> {
    public static final CodecMap<EntityInfoContext> CODEC = new CodecMap<>("EntityInfo");

    public static void register() {
        CODEC.register(DefaultEntityInfoContext.ID, DefaultEntityInfoContext.CODEC);
        CODEC.register(EntityInfoWithPredicate.ID, EntityInfoWithPredicate.CODEC);
    }

    public abstract EntityType<?> getEntityType();

    public abstract int getSpawnAmount();

    public abstract AmountContext getAmount();

    public abstract Codec<? extends EntityInfoContext> getCodec();
}
