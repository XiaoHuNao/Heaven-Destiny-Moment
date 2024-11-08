package com.xiaohunao.heaven_destiny_moment.common.context.entity_info;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.IAmountContext;
import com.xiaohunao.heaven_destiny_moment.common.init.MomentRegistries;
import net.minecraft.world.entity.EntityType;

import java.util.function.Function;

public interface IEntityInfoContext {
    Codec<IEntityInfoContext> CODEC = Codec.lazyInitialized(() -> MomentRegistries.Suppliers.ENTITY_INFO_CODEC.get().byNameCodec()).dispatch(IEntityInfoContext::codec, Function.identity());

    EntityType<?> getEntityType();

    int getSpawnAmount();

    IAmountContext getAmount();

    MapCodec<? extends IEntityInfoContext> codec();
}
