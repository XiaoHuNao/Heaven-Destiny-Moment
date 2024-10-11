package com.xiaohunao.heaven_destiny_moment.common.context.reward;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class XpRewardContext extends RewardContext{
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("xp");
    public static final Codec<XpRewardContext> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("xp").forGetter(XpRewardContext::getXp)
    ).apply(instance, XpRewardContext::new));
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
    public Codec<? extends XpRewardContext> getCodec() {
        return CODEC;
    }
}
