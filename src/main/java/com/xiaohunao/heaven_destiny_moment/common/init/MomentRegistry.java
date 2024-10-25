package com.xiaohunao.heaven_destiny_moment.common.init;

import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class MomentRegistry {
    public static final ResourceKey<Registry<Moment>> KEY = ResourceKey.createRegistryKey(HeavenDestinyMoment.asResource("moment"));
    public static final DeferredRegister<Moment> MOMENT = DeferredRegister.create(KEY, HeavenDestinyMoment.MODID);
    public static final Registry<Moment> REGISTRY = new RegistryBuilder<>(KEY).create();


}
