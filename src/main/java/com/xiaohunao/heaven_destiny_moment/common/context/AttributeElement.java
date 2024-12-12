package com.xiaohunao.heaven_destiny_moment.common.context;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.List;

public record AttributeElement(Holder<Attribute> attribute, List<AttributeModifier> attributeModifiers) {
        public final static Codec<AttributeElement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BuiltInRegistries.ATTRIBUTE.holderByNameCodec().fieldOf("attribute").forGetter(AttributeElement::attribute),
                AttributeModifier.CODEC.listOf().fieldOf("attribute_modifier").forGetter(AttributeElement::attributeModifiers)
        ).apply(instance, AttributeElement::new));

        public void addAttribute(LivingEntity livingEntity) {
             AttributeInstance attributeInstance = livingEntity.getAttribute(attribute);
             for (AttributeModifier attributeModifier : attributeModifiers) {
                 if (attributeInstance != null) {
                     attributeInstance.addOrReplacePermanentModifier(attributeModifier);
                 }
             }
        }
}