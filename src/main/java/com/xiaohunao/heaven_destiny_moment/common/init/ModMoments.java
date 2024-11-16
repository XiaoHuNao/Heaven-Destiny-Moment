package com.xiaohunao.heaven_destiny_moment.common.init;

import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.context.ClientSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentDataContext;
import com.xiaohunao.heaven_destiny_moment.common.context.TipSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.LocationConditionContext;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.area.LocationArea;
import com.xiaohunao.heaven_destiny_moment.common.moment.moment.DefaultMoment;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.FluidPredicate;
import net.minecraft.advancements.critereon.LightPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.material.Fluids;

import java.util.List;

public class ModMoments {
    public static final ResourceKey<Moment> BLOOD_MOON = HeavenDestinyMoment.asResourceKey(MomentRegistries.Keys.MOMENT, "blood_moon");


    public static void bootstrap(BootstrapContext<Moment> context) {
        context.register(BLOOD_MOON, new DefaultMoment(HeavenDestinyMoment.asResource("terra"),
                LocationArea.EMPTY, MomentDataContext.EMPTY, TipSettingsContext.EMPTY, new ClientSettingsContext.Builder().environmentColor(0xff0400).build()

        ));
    }
}
