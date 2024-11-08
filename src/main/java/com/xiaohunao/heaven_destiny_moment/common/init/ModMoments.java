package com.xiaohunao.heaven_destiny_moment.common.init;

import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.context.ClientSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentDataContext;
import com.xiaohunao.heaven_destiny_moment.common.context.condition.LocationConditionContext;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.area.LocationArea;
import com.xiaohunao.heaven_destiny_moment.common.moment.moment.DefaultMoment;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;


import java.util.Optional;

public class ModMoments {
    public static final ResourceKey<Moment> BLOOD_MOON = HeavenDestinyMoment.asResourceKey(MomentRegistries.Keys.MOMENT, "blood_moon");


    public static void bootstrap(BootstrapContext<Moment> context) {
        context.register(BLOOD_MOON, new DefaultMoment(HeavenDestinyMoment.asResource("terra"),
                new LocationArea(LocationConditionContext.Builder.inStructure(BuiltinStructures.SWAMP_HUT,BuiltinStructures.ANCIENT_CITY).build()),
                MomentDataContext.EMPTY, ClientSettingsContext.EMPTY)
        );
    }
}
