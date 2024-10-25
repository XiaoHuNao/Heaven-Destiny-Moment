package com.xiaohunao.heaven_destiny_moment.common.init;

import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.moment.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class MomentTypeRegistry {
    public static final ResourceKey<Registry<MomentType<?>>> KEY = ResourceKey.createRegistryKey(HeavenDestinyMoment.asResource("moment_type"));
    public static final DeferredRegister<MomentType<?>> MOMENT_TYPE = DeferredRegister.create(KEY, HeavenDestinyMoment.MODID);
    public static final Registry<MomentType<?>> REGISTRY = new RegistryBuilder<>(KEY).create();


//    public static final DeferredHolder<MomentType<?>, MomentType<MomentInstance>> MOB_TYPE = MOMENT_TYPE.register("blood_moon",
//            () -> MomentType.Builder.of(MomentInstance::new,ModMoment.BLOOD_MOON.get()).build());
}
