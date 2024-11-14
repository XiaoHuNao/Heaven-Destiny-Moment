package com.xiaohunao.heaven_destiny_moment.common.moment.area;

import com.mojang.serialization.MapCodec;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.LocationConditionContext;
import com.xiaohunao.heaven_destiny_moment.common.init.ModContextRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;


public record LocationArea(LocationConditionContext locationConditionContext) implements Area {
    public static final MapCodec<LocationArea> CODEC = LocationConditionContext.CODEC.xmap(LocationArea::new, LocationArea::locationConditionContext);

    public static final LocationArea EMPTY = new LocationArea(LocationConditionContext.Builder.location().build());


    @Override
    public MapCodec<? extends Area> codec() {
        return ModContextRegister.LOCATION_AREA.get();
    }

    @Override
    public boolean matches(ServerLevel level, BlockPos pos) {
        return locationConditionContext.matches(level, pos);
    }

    public static class Builder {


        public Area build() {
            return null;
        }
    }
}
