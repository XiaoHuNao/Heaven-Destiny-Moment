package com.xiaohunao.heaven_destiny_moment.common.context.reward;

import com.xiaohunao.heaven_destiny_moment.common.codec.CodecMap;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecProvider;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.world.entity.player.Player;

import java.util.function.BiConsumer;

public abstract class RewardContext implements CodecProvider<RewardContext> {
    public static final CodecMap<RewardContext> CODEC = new CodecMap<>("reward");

    public BiConsumer<MomentInstance, Player> custom;


    public void Custom(BiConsumer<MomentInstance, Player> custom) {
        this.custom = custom;
    }

    public boolean hasCustom() {
        return custom != null;
    }

    public void createRewardCustom(MomentInstance momentInstance, Player player) {
        custom.accept(momentInstance, player);
    }

    public abstract void createReward(MomentInstance momentInstance, Player player);



    public static void register() {
        CODEC.register(XpRewardContext.ID, XpRewardContext.CODEC);
        CODEC.register(AttributeRewardContext.ID, AttributeRewardContext.CODEC);
        CODEC.register(EffectRewardContext.ID, EffectRewardContext.CODEC);
        CODEC.register(ItemRewardContext.ID, ItemRewardContext.CODEC);
    }
}
