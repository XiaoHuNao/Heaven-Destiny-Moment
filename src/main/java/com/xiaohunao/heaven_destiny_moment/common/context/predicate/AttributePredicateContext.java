package com.xiaohunao.heaven_destiny_moment.common.context.predicate;

import com.mojang.serialization.Codec;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.context.AttributeContext;
import com.xiaohunao.heaven_destiny_moment.common.context.WeightedContext;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class AttributePredicateContext extends PredicateContext {
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("attribute");
    public static final Codec<AttributePredicateContext> CODEC = WeightedContext.codec(AttributeContext.CODEC)
            .fieldOf("attributes_info")
            .xmap(AttributePredicateContext::new, AttributePredicateContext::getAttribute)
            .codec();

    public WeightedContext<AttributeContext> attributes = WeightedContext.create();
    public AttributePredicateContext(Holder<Attribute> attribute, ResourceLocation name, double amount, int operation, int weight) {
        attributes.add(new AttributeContext(attribute,name,amount,operation),weight);
    }

    public AttributePredicateContext(WeightedContext<AttributeContext> weightedList) {
        this.attributes = weightedList;
    }

    public WeightedContext<AttributeContext> getAttribute() {
        return attributes;
    }
    @Override
    public Codec<AttributePredicateContext> getCodec() {
        return CODEC;
    }
}
