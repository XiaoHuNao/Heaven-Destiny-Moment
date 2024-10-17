package com.xiaohunao.heaven_destiny_moment.common.moment.coverage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentCoverage;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class LevelCoverage extends MomentCoverage {
    public static final LevelCoverage DEFAULT = new LevelCoverage();

    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("level");
    public static final Codec<LevelCoverage> CODEC = Codec.unit(() -> DEFAULT);

    private LevelCoverage() {

    }

    @Override
    public Codec<? extends MomentCoverage> getCodec() {
        return CODEC;
    }
}
