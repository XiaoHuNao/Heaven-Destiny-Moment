package com.xiaohunao.heaven_destiny_moment.common.moment.area;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.LocationConditionContext;
import com.xiaohunao.heaven_destiny_moment.common.init.ModContextRegister;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.stream.Stream;


public record LocationArea(LocationConditionContext locationConditionContext) implements Area {
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("location");
    public static final MapCodec<LocationArea> CODEC = LocationConditionContext.CODEC.xmap(LocationArea::new, LocationArea::locationConditionContext);

    public static final LocationArea EMPTY = new LocationArea(LocationConditionContext.Builder.location().build());


    @Override
    public MapCodec<? extends Area> codec() {
        return ModContextRegister.LOCATION_AREA.get();
    }

    @Override
    public boolean matches(ServerLevel level, BlockPos pos) {
        return locationConditionContext.matches(level,pos);
    }
}
