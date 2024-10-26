package com.xiaohunao.heaven_destiny_moment.common.terra_moment.init;

import com.xiaohunao.heaven_destiny_moment.common.init.MomentTypeRegistry;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentType;

import com.xiaohunao.heaven_destiny_moment.common.terra_moment.moment.BloodMoonMoment;
import com.xiaohunao.heaven_destiny_moment.common.terra_moment.moment.instance.BloodMoonInstance;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMomentTypes {
    public static final DeferredRegister<MomentType<?>> MOMENT_TYPE = DeferredRegister.create(MomentTypeRegistry.KEY, "terra_moment");

    public static final DeferredHolder<MomentType<?>, MomentType<BloodMoonInstance>> BLOOD_MOON = MOMENT_TYPE.register("blood_moon",
            () -> MomentType.Builder.of(BloodMoonInstance::new, BloodMoonMoment.class).build());
}
