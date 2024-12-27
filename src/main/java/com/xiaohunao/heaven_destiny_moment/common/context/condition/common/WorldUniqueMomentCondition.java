package com.xiaohunao.heaven_destiny_moment.common.context.condition.common;

import com.mojang.serialization.MapCodec;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.ICondition;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMContextRegister;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;

public class WorldUniqueMomentCondition implements ICondition {
    public static final WorldUniqueMomentCondition DEFAULT = new WorldUniqueMomentCondition();
    public static MapCodec<WorldUniqueMomentCondition> CODEC = MapCodec.unit(DEFAULT);

    @Override
    public boolean matches(MomentInstance<?> instance, BlockPos pos, @Nullable ServerPlayer serverPlayer) {
        MomentManager momentManager = MomentManager.of(instance.getLevel());
       return momentManager.getImmutableRunMoments().values().stream().noneMatch(momentInstance ->
               momentInstance.getClass().isInstance(instance)
       );
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return HDMContextRegister.WORLD_UNIQUE_MOMENT.get();
    }
}
