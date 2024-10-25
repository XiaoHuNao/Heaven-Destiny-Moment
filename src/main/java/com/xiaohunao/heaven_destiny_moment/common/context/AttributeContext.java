package com.xiaohunao.heaven_destiny_moment.common.context;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record AttributeContext(Holder<Attribute> attribute, AttributeModifier attributeModifier) {
        public final static Codec<AttributeContext> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BuiltInRegistries.ATTRIBUTE.holderByNameCodec().fieldOf("attribute").forGetter(AttributeContext::attribute),
                AttributeModifier.CODEC.fieldOf("attribute_modifier").forGetter(AttributeContext::attributeModifier)
        ).apply(instance, AttributeContext::new));

        public AttributeContext(Holder<Attribute> attribute, ResourceLocation name, double amount, int operation) {
                this(attribute, new AttributeModifier(name, amount, AttributeModifier.Operation.BY_ID.apply(operation)));
        }
}