package com.xiaohunao.heaven_destiny_moment.common.context.reward;

import com.mojang.serialization.MapCodec;
import com.xiaohunao.heaven_destiny_moment.common.context.Weighted;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMContextRegister;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record ItemReward(Weighted<ItemStack> items) implements IReward {
    public static final MapCodec<ItemReward> CODEC = MapCodec.assumeMapUnsafe(Weighted.codec(ItemStack.CODEC)
            .fieldOf("items")
            .xmap(ItemReward::new, ItemReward::items)
            .codec());

    @Override
    public void createReward(MomentInstance<?> moment, Player player) {
        items.getRandomWeighted().forEach(item -> {
            player.getInventory().add(item);
        });
    }

    @Override
    public MapCodec<? extends IReward> codec() {
        return HDMContextRegister.ITEM_REWARD.get();
    }

    public static class Builder {
        private final Weighted.Builder<ItemStack> builder = new Weighted.Builder<>();

        public Builder randomType(Weighted.RandomType randomType){
            builder.randomType(randomType);
            return this;
        }

        public ItemReward build() {
            return new ItemReward(builder.build());
        }

        public Builder add(ItemStack itemStack, int weight) {
            builder.add(itemStack, weight);
            return this;
        }

        public Builder add(ItemStack itemStack) {
            builder.add(itemStack,1);
            return this;
        }


    }

}
