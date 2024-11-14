package com.xiaohunao.heaven_destiny_moment.common.context;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecExtra;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.List;
import java.util.Set;

public record AttributeContext(Holder<Attribute> attribute, List<AttributeModifier> attributeModifiers) {
        public final static Codec<AttributeContext> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BuiltInRegistries.ATTRIBUTE.holderByNameCodec().fieldOf("attribute").forGetter(AttributeContext::attribute),
                AttributeModifier.CODEC.listOf().fieldOf("attribute_modifier").forGetter(AttributeContext::attributeModifiers)
        ).apply(instance, AttributeContext::new));

        public void addAttribute(LivingEntity livingEntity) {

        }
}