package com.xiaohunao.heaven_destiny_moment.common.context.reward;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMContextRegister;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.world.entity.player.Player;

public record XpReward(int xp) implements IReward {
    public static final MapCodec<XpReward> CODEC = MapCodec.assumeMapUnsafe(RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("xp").forGetter(XpReward::getXp)
    ).apply(instance, XpReward::new)));

    @Override
    public void createReward(MomentInstance moment, Player player) {
        player.giveExperiencePoints(xp);
    }

    public int getXp() {
        return xp;
    }

    @Override
    public MapCodec<? extends IReward> codec() {
        return HDMContextRegister.XP_REWARD.get();
    }
}
