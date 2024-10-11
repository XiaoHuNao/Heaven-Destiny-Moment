package com.xiaohunao.heaven_destiny_moment.common.context.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.context.WeightedContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class EquipmentPredicateContext extends PredicateContext {
    public static final ResourceLocation ID = HeavenDestinyMoment.asResource("equipment");
    public static final Codec<EquipmentPredicateContext> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            WeightedContext.codec(ItemStack.CODEC).fieldOf("items").forGetter(EquipmentPredicateContext::getEquipment),
            Codec.BOOL.fieldOf("can_drop").forGetter(EquipmentPredicateContext::canDrop)
    ).apply(instance, EquipmentPredicateContext::new));

    public WeightedContext<ItemStack> equipment = WeightedContext.create();
    public boolean canDrop;
    public EquipmentPredicateContext(WeightedContext<ItemStack> equipment, boolean canDrop) {
        this.equipment = equipment;
        this.canDrop = canDrop;
    }
    public EquipmentPredicateContext(ItemStack item, int weight) {
        equipment.add(item, weight);
    }
    public EquipmentPredicateContext add(ItemStack item, int weight){
        equipment.add(item,weight);
        return this;
    }
    public WeightedContext<ItemStack> getEquipment() {
        return equipment;
    }
    public Boolean canDrop() {
        return canDrop;
    }

    @Override
    public Codec<EquipmentPredicateContext> getCodec() {
        return CODEC;
    }
}
