package com.xiaohunao.heaven_destiny_moment.common.context.condition;

import com.mojang.serialization.Codec;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;


public class KillStatisticsConditionContext extends ConditionContext{
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("kill_statistics");

    public static final Codec<KillStatisticsConditionContext> CODEC = Codec.INT.xmap(KillStatisticsConditionContext::new, context -> context.killCount);

    public int killCount;

    public KillStatisticsConditionContext(int killCount) {
        this.killCount = killCount;
    }

    @Override
    public boolean test(MomentInstance MomentInstance, Level level, BlockPos pos, Player player) {
        return MomentInstance.getKillCount() >= killCount;
    }

    @Override
    public Codec<? extends ConditionContext> getCodec() {
        return null;
    }
}
