package com.xiaohunao.heaven_destiny_moment.common.init;

import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentType;
import com.xiaohunao.heaven_destiny_moment.common.moment.moment.DefaultMoment;
import com.xiaohunao.heaven_destiny_moment.common.moment.moment.instance.DefaultInstance;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class HDMMomentTypes {
    public static final DeferredRegister<MomentType<?>> MOMENT_TYPE = DeferredRegister.create(HDMRegistries.MOMENT_TYPE,HeavenDestinyMoment.MODID);


    public static final DeferredHolder<MomentType<?>, MomentType<DefaultInstance>> DEFAULT = MOMENT_TYPE.register("default",
            () -> MomentType.builder(DefaultInstance::new, DefaultMoment.class).build());
}
