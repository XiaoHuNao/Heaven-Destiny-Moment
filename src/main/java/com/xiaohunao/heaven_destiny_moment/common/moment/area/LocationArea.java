package com.xiaohunao.heaven_destiny_moment.common.moment.area;

import com.mojang.serialization.MapCodec;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.LocationConditionContext;
import com.xiaohunao.heaven_destiny_moment.common.init.ModContextRegister;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.FluidPredicate;
import net.minecraft.advancements.critereon.LightPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;


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
        public Area build(Function<LocationConditionContext.Builder,LocationConditionContext> function) {
            return new LocationArea(function.apply(new LocationConditionContext.Builder()));
        }
    }
}
