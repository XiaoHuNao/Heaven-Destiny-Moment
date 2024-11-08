package com.xiaohunao.heaven_destiny_moment.common.context.reward;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.init.ModContextRegister;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class XpRewardContext implements IRewardContext {
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("xp");
    public static final MapCodec<XpRewardContext> CODEC = MapCodec.assumeMapUnsafe(RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("xp").forGetter(XpRewardContext::getXp)
    ).apply(instance, XpRewardContext::new)));
    private int xp;
    public XpRewardContext(int xp) {
        this.xp = xp;
    }

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
