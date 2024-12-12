package com.xiaohunao.heaven_destiny_moment.common.context.reward;


import com.mojang.serialization.MapCodec;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.context.AttributeElement;
import com.xiaohunao.heaven_destiny_moment.common.context.Weighted;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMContextRegister;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;


public record AttributeReward(Weighted<AttributeElement> attributes) implements IReward {
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("attribute");
    public static final MapCodec<AttributeReward> CODEC = MapCodec.assumeMapUnsafe(Weighted.codec(AttributeElement.CODEC)
            .fieldOf("attributes")
            .xmap(AttributeReward::new, AttributeReward::attributes)
            .codec());

    @Override
    public void createReward(MomentInstance momentInstance, Player player) {
        Level serverLevel = momentInstance.getLevel();
        attributes.getRandomValue(serverLevel.random).ifPresent(reward -> {
            reward.attributeModifiers().forEach(attributeModifier -> {
                AttributeInstance instance = player.getAttribute(reward.attribute());
                if (instance != null) {
                    instance.removeModifier(attributeModifier);
                    instance.addPermanentModifier(attributeModifier);
                }
            });
        });
    }

    @Override
    public MapCodec<? extends IReward> codec() {
        return HDMContextRegister.ATTRIBUTE_REWARD.get();
    }
}
