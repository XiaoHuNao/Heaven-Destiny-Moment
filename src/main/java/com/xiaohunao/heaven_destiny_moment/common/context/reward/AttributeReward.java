package com.xiaohunao.heaven_destiny_moment.common.context.reward;


import com.mojang.serialization.MapCodec;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.context.AttributeElement;
import com.xiaohunao.heaven_destiny_moment.common.context.Weighted;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMContextRegister;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;


public record AttributeReward(Weighted<AttributeElement> attributes) implements IReward {
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("attribute");
    public static final MapCodec<AttributeReward> CODEC = MapCodec.assumeMapUnsafe(Weighted.codec(AttributeElement.CODEC)
            .fieldOf("attributes")
            .xmap(AttributeReward::new, AttributeReward::attributes)
            .codec());

    @Override
    public void createReward(MomentInstance<?> momentInstance, Player player) {
        attributes.getRandomWeighted().forEach(reward -> {
            AttributeModifier attributeModifier = reward.attributeModifier();
            AttributeInstance instance = player.getAttribute(reward.attribute());
            if (instance != null) {
                instance.removeModifier(attributeModifier);
                instance.addPermanentModifier(attributeModifier);
            }
        });
    }

    @Override
    public MapCodec<? extends IReward> codec() {
        return HDMContextRegister.ATTRIBUTE_REWARD.get();
    }

    public static class Builder {
        private final Weighted.Builder<AttributeElement> builder = new Weighted.Builder<>();

        public AttributeReward build() {
            return new AttributeReward(builder.build());
        }
        public Builder randomType(Weighted.RandomType randomType){
            builder.randomType(randomType);
            return this;
        }

        public Builder add(AttributeElement element) {
            builder.add(element,1);
            return this;
        }

        public Builder add(AttributeElement element, int weight) {
            builder.add(element,weight);
            return this;
        }
    }


}
