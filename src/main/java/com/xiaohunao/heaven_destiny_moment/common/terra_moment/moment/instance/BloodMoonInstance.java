package com.xiaohunao.heaven_destiny_moment.common.terra_moment.moment.instance;

import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import com.xiaohunao.heaven_destiny_moment.common.terra_moment.init.ModMomentTypes;
import net.minecraft.world.level.Level;

public class BloodMoonInstance extends MomentInstance {
    public BloodMoonInstance(Level level, Moment moment) {
        super(ModMomentTypes.BLOOD_MOON.get(), level, moment);
    }
}
