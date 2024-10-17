package com.xiaohunao.heaven_destiny_moment.common.moment;

import com.mojang.serialization.Codec;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecMap;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecProvider;
import com.xiaohunao.heaven_destiny_moment.common.moment.coverage.AreaCoverage;
import com.xiaohunao.heaven_destiny_moment.common.moment.coverage.LevelCoverage;

public abstract class MomentCoverage implements CodecProvider<MomentCoverage> {
    public static final CodecMap<MomentCoverage> CODEC = new CodecMap<>("moment_coverage");


    public static void register() {
        CODEC.register(LevelCoverage.ID, LevelCoverage.CODEC);
        CODEC.register(AreaCoverage.ID, AreaCoverage.CODEC);
    }
}
