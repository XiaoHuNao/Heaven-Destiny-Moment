package com.xiaohunao.heaven_destiny_moment.common.context.reward;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.context.WeightedContext;
import com.xiaohunao.heaven_destiny_moment.common.init.ModContextRegister;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ItemRewardContext implements IRewardContext {
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("item");
    public static final MapCodec<ItemRewardContext> CODEC = MapCodec.assumeMapUnsafe(WeightedContext.codec(ItemStack.CODEC)
            .fieldOf("items")
            .xmap(ItemRewardContext::new, ItemRewardContext::getItem)
            .codec());
    public WeightedContext<ItemStack> reward = WeightedContext.create();


    public ItemRewardContext(WeightedContext<ItemStack> reward) {
        this.reward = reward;
    }

    public ItemRewardContext(ItemStack stack, int weight) {
        addItem(stack, weight);
    }
    public ItemRewardContext(ItemStack stack) {
        addItem(stack, 1);
    }

    public WeightedContext<ItemStack> getItem() {
        return reward;
    }

    public void createReward(MomentInstance moment, Player player) {
        reward.getRandomValue(moment.getLevel().random).ifPresent(item -> {
            player.getInventory().add(item);
        });
    }

    public void addItem(ItemStack stack, int weight){
        reward.add(stack,weight);
    }

    @Override
    public MapCodec<? extends IRewardContext> codec() {
        return ModContextRegister.ITEM_REWARD.get();
    }

}
