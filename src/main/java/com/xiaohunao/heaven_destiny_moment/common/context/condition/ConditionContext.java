package com.xiaohunao.heaven_destiny_moment.common.context.condition;

import com.mojang.serialization.Codec;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecMap;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecProvider;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public abstract class ConditionContext implements CodecProvider<ConditionContext> {
    public static final CodecMap<ConditionContext> CODEC = new CodecMap<>("Condition");

    public abstract boolean test(MomentInstance moment, Level level, BlockPos pos, Player player);

    @Override
    public abstract Codec<? extends ConditionContext> getCodec();



    public static void register() {
        CODEC.register(BiomesConditionContext.ID, BiomesConditionContext.CODEC);
        CODEC.register(StructureConditionContext.ID, StructureConditionContext.CODEC);
        CODEC.register(WeatherConditionContext.ID, WeatherConditionContext.CODEC);
        CODEC.register(XpConditionContext.ID, XpConditionContext.CODEC);
        CODEC.register(YAxisHeightConditionContext.ID, YAxisHeightConditionContext.CODEC);
        CODEC.register(TimeConditionContext.ID,TimeConditionContext.CODEC);
    }
}