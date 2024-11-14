package com.xiaohunao.heaven_destiny_moment.common.context.attachable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.xiaohunao.heaven_destiny_moment.common.init.MomentRegistries;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Function;

public interface IAttachable {
    Codec<IAttachable> CODEC = Codec.lazyInitialized(() -> MomentRegistries.Suppliers.ATTACHABLE_CODEC.get().byNameCodec()).dispatch(IAttachable::codec, Function.identity());

    void attachToEntity(LivingEntity livingEntity);

    MapCodec<? extends IAttachable> codec();
}
