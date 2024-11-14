package com.xiaohunao.heaven_destiny_moment.common.context.attachable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.context.AttributeContext;
import com.xiaohunao.heaven_destiny_moment.common.context.equippable_slot.IEquippableSlot;
import com.xiaohunao.heaven_destiny_moment.common.init.ModContextRegister;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record CommonAttachable(Optional<List<MobEffectInstance>> effects, Optional<Map<IEquippableSlot, ItemStack>> equipments, Optional<List<AttributeContext>> attributes) implements IAttachable{
    public final static MapCodec<CommonAttachable> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            MobEffectInstance.CODEC.listOf().optionalFieldOf("effects").forGetter(CommonAttachable::effects),
            Codec.unboundedMap(IEquippableSlot.CODEC,ItemStack.CODEC).optionalFieldOf("equipments").forGetter(CommonAttachable::equipments),
            AttributeContext.CODEC.listOf().optionalFieldOf("attributes").forGetter(CommonAttachable::attributes)
    ).apply(instance, CommonAttachable::new));

    @Override
    public void attachToEntity(LivingEntity livingEntity) {
        effects.ifPresent(mobEffectInstances -> {
            mobEffectInstances.forEach(livingEntity::addEffect);
        });
        equipments.ifPresent(equipment -> {
            equipment.forEach(((slot, stack) -> slot.wear(livingEntity,stack)));
        });
        attributes.ifPresent(attributeContexts -> {
            attributeContexts.forEach(attributeContext -> {
                attributeContext.addAttribute(livingEntity);
            });
        });
    }

    @Override
    public MapCodec<? extends IAttachable> codec() {
        return ModContextRegister.COMMON_ATTACHABLE.get();
    }

    public static class Builder {
        Optional<List<MobEffectInstance>> effects = Optional.empty();
        Optional<Map<IEquippableSlot, ItemStack>> equipments = Optional.empty();
        Optional<List<AttributeContext>> attributes = Optional.empty();

        public CommonAttachable build(){
           return new CommonAttachable(effects,equipments,attributes);
        }


        public Builder addEffect(Holder<MobEffect> effect, int duration, int amplifier){
            if (effects.isEmpty()){
                effects = Optional.of(Lists.newArrayList());
            }
            effects.get().add(new MobEffectInstance(effect,duration,amplifier));
            return this;
        }

        public Builder addEffect(Holder<MobEffect> effect, int duration) {
            addEffect(effect,duration,0);
            return this;
        }

        public Builder addEquipment(IEquippableSlot slot,ItemStack stack){
            if (equipments.isEmpty()) {
                equipments = Optional.of(Maps.newHashMap());
            }
            equipments.get().put(slot,stack);
            return this;
        }

        public Builder addAttribute(Holder<Attribute> attribute, List<AttributeModifier> attributeModifiers){
            if (attributes.isEmpty()) {
                attributes = Optional.of(Lists.newArrayList());
            }
            attributes.get().add(new AttributeContext(attribute,attributeModifiers));
            return this;
        }
    }
}
