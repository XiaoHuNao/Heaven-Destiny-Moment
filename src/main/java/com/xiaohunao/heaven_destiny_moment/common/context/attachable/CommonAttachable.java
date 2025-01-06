package com.xiaohunao.heaven_destiny_moment.common.context.attachable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.context.AttributeElement;
import com.xiaohunao.heaven_destiny_moment.common.context.Weighted;
import com.xiaohunao.heaven_destiny_moment.common.context.equippable_slot.IEquippableSlot;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMContextRegister;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record CommonAttachable(Optional<Weighted<MobEffectInstance>> effects, Optional<Weighted<Pair<IEquippableSlot, ItemStack>>> equipments, Optional<Weighted<AttributeElement>> attributes) implements IAttachable{
    public final static MapCodec<CommonAttachable> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Weighted.codec(MobEffectInstance.CODEC).optionalFieldOf("effects").forGetter(CommonAttachable::effects),
            Weighted.codec(Codec.pair(IEquippableSlot.CODEC,ItemStack.CODEC)).optionalFieldOf("equipments").forGetter(CommonAttachable::equipments),
            Weighted.codec(AttributeElement.CODEC).optionalFieldOf("attributes").forGetter(CommonAttachable::attributes)
    ).apply(instance, CommonAttachable::new));

    @Override
    public void attachToEntity(LivingEntity livingEntity) {
        effects.ifPresent(mobEffectInstances -> {
            mobEffectInstances.getRandomSubset().forEach(livingEntity::addEffect);
        });
        equipments.ifPresent(equipment -> {
            equipment.getRandomSubset().forEach((pair -> pair.getFirst().wear(livingEntity,pair.getSecond())));
        });
        attributes.ifPresent(attributeContexts -> {
            attributeContexts.getRandomSubset().forEach(attributeElement -> {
                attributeElement.addAttribute(livingEntity);
            });
        });
    }

    @Override
    public MapCodec<? extends IAttachable> codec() {
        return HDMContextRegister.COMMON_ATTACHABLE.get();
    }

    public static class Builder {
        private Weighted.Builder<MobEffectInstance> effects;
        private Weighted.Builder<Pair<IEquippableSlot, ItemStack>> equipments;
        private Weighted.Builder<AttributeElement> attributes;

        public CommonAttachable build(){
           return new CommonAttachable(Optional.ofNullable(effects.build()),Optional.ofNullable(equipments.build()),Optional.ofNullable(attributes.build()));
        }


        public Builder addEffect(Holder<MobEffect> effect, int duration, int amplifier){
            if (effects == null){
                effects = new Weighted.Builder<>();
            }
            effects.add(new MobEffectInstance(effect,duration,amplifier),1);
            return this;
        }

        public Builder addEffect(Holder<MobEffect> effect, int duration, int amplifier, int weight){
            if (effects == null){
                effects = new Weighted.Builder<>();
            }
            effects.add(new MobEffectInstance(effect,duration,amplifier),weight);
            return this;
        }

        public Builder addEquipment(IEquippableSlot slot,ItemStack stack){
            if (equipments == null) {
                equipments = new Weighted.Builder<>();
            }
            equipments.add(new Pair<>(slot,stack),1);
            return this;
        }

        public Builder addEquipment(IEquippableSlot slot,ItemStack stack, int weight){
            if (equipments == null) {
                equipments = new Weighted.Builder<>();
            }
            equipments.add(new Pair<>(slot,stack),weight);
            return this;
        }

        public Builder addAttribute(Holder<Attribute> attribute, ResourceLocation id, double amount, AttributeModifier.Operation operation){
            if (attributes == null) {
                attributes = new Weighted.Builder<>();
            }
            attributes.add(new AttributeElement(attribute,new AttributeModifier(id,amount,operation)),1);
            return this;
        }

        public Builder addAttribute(Holder<Attribute> attribute, ResourceLocation id, double amount, AttributeModifier.Operation operation,int weight){
            if (attributes == null) {
                attributes = new Weighted.Builder<>();
            }
            attributes.add(new AttributeElement(attribute,new AttributeModifier(id,amount,operation)),weight);
            return this;
        }
    }
}
