package com.xiaohunao.heaven_destiny_moment.common.context.condition;

import com.mojang.serialization.Codec;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class BattlePointConditionContext extends ConditionContext{
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("battle_point");
    public static final Codec<BattlePointConditionContext> CODEC = Codec.INT.xmap(BattlePointConditionContext::new, context -> context.battlePoint);

    public int battlePoint;

    public BattlePointConditionContext(int battlePoint) {
        this.battlePoint = battlePoint;
    }

    @Override
    public boolean test(MomentInstance MomentInstance, Level level, BlockPos pos, Player player) {
        return MomentInstance.getBattlePoint() >= battlePoint;
    }

    @Override
    public Codec<? extends ConditionContext> getCodec() {
        return CODEC;
    }
}
