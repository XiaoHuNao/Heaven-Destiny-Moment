package com.xiaohunao.heaven_destiny_moment.common.context;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.codec.CodecExtra;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;

public record AttributeContext(Attribute attribute, AttributeModifier attributeModifier) {
        public final static Codec<AttributeContext> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ForgeRegistries.ATTRIBUTES.getCodec().fieldOf("attribute").forGetter(AttributeContext::attribute),
                CodecExtra.ATTRIBUTE_MODIFIER_CODEC.fieldOf("attribute_modifier").forGetter(AttributeContext::attributeModifier)
        ).apply(instance, AttributeContext::new));

        public AttributeContext(Attribute attribute, String name, double amount, int operation) {
                this(attribute, new AttributeModifier(name, amount, AttributeModifier.Operation.fromValue(operation)));
        }
}