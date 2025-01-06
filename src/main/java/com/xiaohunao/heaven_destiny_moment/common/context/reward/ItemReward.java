package com.xiaohunao.heaven_destiny_moment.common.context.reward;

import com.mojang.serialization.MapCodec;
import com.xiaohunao.heaven_destiny_moment.common.context.Weighted;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMContextRegister;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record ItemReward(Weighted<ItemStack> reward) implements IReward {
    public static final MapCodec<ItemReward> CODEC = MapCodec.assumeMapUnsafe(Weighted.codec(ItemStack.CODEC)
            .fieldOf("items")
            .xmap(ItemReward::new, ItemReward::reward)
            .codec());

    @Override
    public void createReward(MomentInstance<?> moment, Player player) {
        reward.getRandomValue().ifPresent(item -> {
            player.getInventory().add(item);
        });
    }

    @Override
    public MapCodec<? extends IReward> codec() {
        return HDMContextRegister.ITEM_REWARD.get();
    }

    public static class Builder {
        private final Weighted.Builder<ItemStack> builder = new Weighted.Builder<>();


    }

}
