package com.xiaohunao.heaven_destiny_moment.common.moment.moment.instance;

import com.xiaohunao.heaven_destiny_moment.common.init.ModMomentTypes;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class BloodMoonInstance extends MomentInstance {
    public BloodMoonInstance(Level level, ResourceKey<Moment> moment) {
        super(ModMomentTypes.BLOOD_MOON.get(), level, moment);
    }
}
