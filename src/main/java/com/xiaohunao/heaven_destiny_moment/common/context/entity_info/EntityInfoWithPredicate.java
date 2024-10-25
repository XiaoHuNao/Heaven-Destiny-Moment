package com.xiaohunao.heaven_destiny_moment.common.context.entity_info;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.context.amount.AmountContext;
import com.xiaohunao.heaven_destiny_moment.common.context.predicate.PredicateContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class EntityInfoWithPredicate extends DefaultEntityInfoContext {
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("entity_info_with_predicate");
    public static final Codec<EntityInfoWithPredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(DefaultEntityInfoContext::getEntityType),
            AmountContext.CODEC.fieldOf("amount_module").forGetter(DefaultEntityInfoContext::getAmount),
            Codec.list(PredicateContext.CODEC).fieldOf("predicates").forGetter(EntityInfoWithPredicate::getPredicates)
    ).apply(instance, EntityInfoWithPredicate::new));

    private final List<PredicateContext> predicates = Lists.newArrayList();

    public EntityInfoWithPredicate(EntityType<?> entityType, int amount, PredicateContext... predicates) {
        super(entityType, amount);
        this.predicates.addAll(List.of(predicates));
    }

    public EntityInfoWithPredicate(EntityType<?> entityType, int min, int max, PredicateContext... predicates) {
        super(entityType, min, max);
        this.predicates.addAll(List.of(predicates));
    }

    public EntityInfoWithPredicate(EntityType<?> entityType, AmountContext amount , PredicateContext... predicates) {
        super(entityType, amount);
        this.predicates.addAll(List.of(predicates));
    }
    public EntityInfoWithPredicate(EntityType<?> entityType, AmountContext amount, List<PredicateContext> IPredicates) {
        super(entityType, amount);
        this.predicates.addAll(IPredicates);
    }

    public List<PredicateContext> getPredicates() {
        return predicates;
    }

    @Override
    public Codec<? extends EntityInfoContext> getCodec() {
        return CODEC;
    }
}
