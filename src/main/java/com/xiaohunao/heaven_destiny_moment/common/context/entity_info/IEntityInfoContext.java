package com.xiaohunao.heaven_destiny_moment.common.context.entity_info;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.IAmountContext;
import com.xiaohunao.heaven_destiny_moment.common.init.MomentRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.function.Function;

public interface IEntityInfoContext  {
    Codec<IEntityInfoContext> CODEC = Codec.lazyInitialized(() -> MomentRegistries.Suppliers.ENTITY_INFO_CODEC.get().byNameCodec()).dispatch(IEntityInfoContext::codec, Function.identity());

    MapCodec<? extends IEntityInfoContext> codec();
}
