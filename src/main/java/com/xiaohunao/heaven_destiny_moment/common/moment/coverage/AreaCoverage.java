package com.xiaohunao.heaven_destiny_moment.common.moment.coverage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentCoverage;
import net.minecraft.resources.ResourceLocation;

public class AreaCoverage extends MomentCoverage {
    public static final AreaCoverage DEFAULT = new AreaCoverage();

    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("area");
    public static final Codec<AreaCoverage> CODEC = Codec.unit(() -> DEFAULT);

    private AreaCoverage() {

    }

    @Override
    public Codec<? extends MomentCoverage> getCodec() {
        return CODEC;
    }
}
