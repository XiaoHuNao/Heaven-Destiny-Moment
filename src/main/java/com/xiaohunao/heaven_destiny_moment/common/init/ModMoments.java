package com.xiaohunao.heaven_destiny_moment.common.init;

import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.context.ClientSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentDataContext;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.LocationConditionContext;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.area.LocationArea;
import com.xiaohunao.heaven_destiny_moment.common.moment.moment.DefaultMoment;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.FluidPredicate;
import net.minecraft.advancements.critereon.LightPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.DimensionTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;


import java.util.List;
import java.util.Optional;

public class ModMoments {
    public static final ResourceKey<Moment> BLOOD_MOON = HeavenDestinyMoment.asResourceKey(MomentRegistries.Keys.MOMENT, "blood_moon");


    public static void bootstrap(BootstrapContext<Moment> context) {
        context.register(BLOOD_MOON, new DefaultMoment(HeavenDestinyMoment.asResource("terra"),
                new LocationArea(LocationConditionContext.Builder.location().setStructures(List.of(BuiltinStructures.SWAMP_HUT, BuiltinStructures.ANCIENT_CITY))
                        .setBiomes(List.of(Biomes.BADLANDS))
                        .setDimension(List.of(Level.END,Level.OVERWORLD))
                        .setY(MinMaxBounds.Doubles.between(10,50))
                        .setBlock(BlockPredicate.Builder.block().of(Blocks.DIAMOND_BLOCK))
                        .setCanSeeSky(false)
                        .setFluid(FluidPredicate.Builder.fluid().of(Fluids.LAVA))
                        .setLight(LightPredicate.Builder.light().setComposite(MinMaxBounds.Ints.between(2,5)))
                        .setSmokey(true)
                        .build()
                ),
                MomentDataContext.EMPTY, ClientSettingsContext.EMPTY)
        );
    }
}
