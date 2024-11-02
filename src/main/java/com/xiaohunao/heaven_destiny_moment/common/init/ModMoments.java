package com.xiaohunao.heaven_destiny_moment.common.init;

import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.context.ClientSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentDataContext;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.area.LocationArea;
import com.xiaohunao.heaven_destiny_moment.common.moment.moment.DefaultMoment;
import com.xiaohunao.heaven_destiny_moment.common.utils.HolderUtils;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;

public class ModMoments {
    public static final ResourceKey<Moment> BLOOD_MOON = HeavenDestinyMoment.asResourceKey(MomentRegistries.Keys.MOMENT, "blood_moon");


    public static void bootstrap(BootstrapContext<Moment> context) {
        HolderGetter<Structure> structureHolderGetter = context.lookup(Registries.STRUCTURE);

        HolderSet<Structure> holderSet = HolderUtils.getHolderSet(structureHolderGetter, BuiltinStructures.SWAMP_HUT,BuiltinStructures.ANCIENT_CITY);
        context.register(BLOOD_MOON, new DefaultMoment(HeavenDestinyMoment.asResource("terra"),
                new LocationArea(LocationPredicate.Builder.location().setStructures(holderSet).build()),
                MomentDataContext.EMPTY, ClientSettingsContext.EMPTY)
        );
    }
}
