package com.xiaohunao.heaven_destiny_moment.common.context.equippable_slot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xiaohunao.heaven_destiny_moment.common.init.HDMContextRegister;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

public record VanillaEquippableSlot(EquipmentSlot slot) implements IEquippableSlot{
    public static final MapCodec<VanillaEquippableSlot> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            EquipmentSlot.CODEC.fieldOf("slot").forGetter(VanillaEquippableSlot::slot)
    ).apply(instance, VanillaEquippableSlot::new));


    public static VanillaEquippableSlot of(EquipmentSlot slot) {
        return new VanillaEquippableSlot(slot);
    }


    @Override
    public MapCodec<? extends IEquippableSlot> codec() {
        return HDMContextRegister.VANILLA_EQUIPPABLE_SLOT.get();
    }

    @Override
    public void wear(LivingEntity livingEntity, ItemStack stack) {
        if (livingEntity instanceof Mob mob){
            mob.equipItemIfPossible(stack);
        }
    }
}
