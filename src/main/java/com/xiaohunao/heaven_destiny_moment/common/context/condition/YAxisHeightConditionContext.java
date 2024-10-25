package com.xiaohunao.heaven_destiny_moment.common.context.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class YAxisHeightConditionContext extends ConditionContext {
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("y_axis_height");
    public static final Codec<YAxisHeightConditionContext> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("y_axis").forGetter(YAxisHeightConditionContext::getYAxisHeight),
            Codec.INT.fieldOf("flag").forGetter(YAxisHeightConditionContext::getFlag)
    ).apply(instance, YAxisHeightConditionContext::new));
    private final int yAxis;
    private final int flag;

    public YAxisHeightConditionContext(int yAxis, int flag) {
        this.yAxis = yAxis;
        this.flag = flag;
    }

    public int getYAxisHeight() {
        return yAxis;
    }

    public int getFlag() {
        return flag;
    }

    @Override
    public boolean test(MomentInstance moment, Level level, BlockPos pos, Player player) {
        return switch (flag) {
            case 0 -> pos.getY() == yAxis;
            case 1 -> pos.getY() > yAxis;
            case 2 -> pos.getY() < yAxis;
            case 3 -> pos.getY() >= yAxis;
            case 4 -> pos.getY() <= yAxis;
            default -> false;
        };
    }

    @Override
    public Codec<YAxisHeightConditionContext> getCodec() {
        return CODEC;
    }
}
