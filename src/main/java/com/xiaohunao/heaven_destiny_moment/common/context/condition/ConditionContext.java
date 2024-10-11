package com.xiaohunao.heaven_destiny_moment.common.context.condition;

import com.mojang.serialization.Codec;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecMap;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecProvider;
import com.xiaohunao.heaven_destiny_moment.common.function.QuadPredicate;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Objects;

public abstract class ConditionContext implements CodecProvider<ConditionContext> {
    public static final CodecMap<ConditionContext> CODEC = new CodecMap<>("Condition");

    protected QuadPredicate<MomentInstance,Level, BlockPos, Player> custom;

    public void Custom(QuadPredicate<MomentInstance,Level, BlockPos, Player> predicate) {
        custom = Objects.requireNonNull(predicate, "predicate cannot be null");
    }
    public boolean hasCustom() {
        return custom != null;
    }

    public boolean canCreateCustom(MomentInstance MomentInstance, Level level, BlockPos pos, Player player) {
        return custom.test(MomentInstance, level, pos, player);
    }

    public abstract boolean canCreate(MomentInstance MomentInstance, Level level, BlockPos pos, Player player);

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