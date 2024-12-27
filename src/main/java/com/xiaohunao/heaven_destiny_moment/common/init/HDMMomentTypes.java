package com.xiaohunao.heaven_destiny_moment.common.init;

import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentType;
import com.xiaohunao.heaven_destiny_moment.common.moment.moment.instance.DefaultInstance;
import com.xiaohunao.heaven_destiny_moment.common.moment.moment.instance.RaidInstance;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class HDMMomentTypes {
    public static final DeferredRegister<MomentType<?>> MOMENT_TYPE = DeferredRegister.create(HDMRegistries.MOMENT_TYPE,HeavenDestinyMoment.MODID);


    public static final DeferredHolder<MomentType<?>, MomentType<DefaultInstance>> DEFAULT = MOMENT_TYPE.register("default",
            () -> new MomentType.Builder<>(DefaultInstance::new).build());

    public static final DeferredHolder<MomentType<?>, MomentType<RaidInstance>> RAID = MOMENT_TYPE.register("raid",
            () -> new MomentType.Builder<>(RaidInstance::new).build());
}
