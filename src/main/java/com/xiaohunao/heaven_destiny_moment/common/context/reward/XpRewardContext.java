package com.xiaohunao.heaven_destiny_moment.common.context.reward;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.init.ModContextRegister;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.world.entity.player.Player;

public record XpRewardContext(int xp) implements IRewardContext {
    public static final MapCodec<XpRewardContext> CODEC = MapCodec.assumeMapUnsafe(RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("xp").forGetter(XpRewardContext::getXp)
    ).apply(instance, XpRewardContext::new)));

    @Override
    public void createReward(MomentInstance moment, Player player) {
        player.giveExperiencePoints(xp);
    }

    public int getXp() {
        return xp;
    }

    @Override
    public MapCodec<? extends IRewardContext> codec() {
        return ModContextRegister.XP_REWARD.get();
    }
}
