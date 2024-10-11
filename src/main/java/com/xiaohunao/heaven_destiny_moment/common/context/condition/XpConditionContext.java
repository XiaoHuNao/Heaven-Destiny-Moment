package com.xiaohunao.heaven_destiny_moment.common.context.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class XpConditionContext extends ConditionContext {
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("xp");
    public static final Codec<XpConditionContext> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("xp").forGetter(XpConditionContext::getXp)
    ).apply(instance, XpConditionContext::new));
    private final int xp;

    public XpConditionContext(int xp) {
        this.xp = xp;
    }

    public int getXp() {
        return xp;
    }

    @Override
    public boolean canCreate(MomentInstance moment, Level level, BlockPos pos, Player player) {
        if (player == null) return false;
        return player.experienceLevel >= xp;
    }

    @Override
    public Codec<XpConditionContext> getCodec() {
        return CODEC;
    }
}
