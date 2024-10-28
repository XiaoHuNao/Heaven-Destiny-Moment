package com.xiaohunao.heaven_destiny_moment.common.init;

import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentType;
import com.xiaohunao.heaven_destiny_moment.common.moment.moment.BloodMoonMoment;
import com.xiaohunao.heaven_destiny_moment.common.moment.moment.instance.BloodMoonInstance;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class ModMomentTypes {
    public static final DeferredRegister<MomentType<?>> MOMENT_TYPE = DeferredRegister.create(MomentRegistries.MOMENT_TYPE,HeavenDestinyMoment.MODID);


    public static final DeferredHolder<MomentType<?>, MomentType<BloodMoonInstance>> BLOOD_MOON = MOMENT_TYPE.register("blood_moon",
            () -> MomentType.Builder.of(BloodMoonInstance::new, BloodMoonMoment.class).build());
}
