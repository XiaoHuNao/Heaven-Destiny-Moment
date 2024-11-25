package com.xiaohunao.heaven_destiny_moment.common.context.reward;

import com.mojang.serialization.MapCodec;
import com.xiaohunao.heaven_destiny_moment.common.context.WeightedContext;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMContextRegister;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record ItemRewardContext(WeightedContext<ItemStack> reward) implements IRewardContext {
    public static final MapCodec<ItemRewardContext> CODEC = MapCodec.assumeMapUnsafe(WeightedContext.codec(ItemStack.CODEC)
            .fieldOf("items")
            .xmap(ItemRewardContext::new, ItemRewardContext::reward)
            .codec());

    @Override
    public void createReward(MomentInstance moment, Player player) {
        reward.getRandomValue(moment.getLevel().random).ifPresent(item -> {
            player.getInventory().add(item);
        });
    }

    @Override
    public MapCodec<? extends IRewardContext> codec() {
        return HDMContextRegister.ITEM_REWARD.get();
    }

    public static class Builder {
        private final WeightedContext.Builder<ItemStack> builder = new WeightedContext.Builder<>();


    }

}
