package com.xiaohunao.heaven_destiny_moment.common.terra_moment.moment;

import com.xiaohunao.heaven_destiny_moment.common.context.ClientSettingsContext;
import com.xiaohunao.heaven_destiny_moment.common.context.MomentDataContext;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import com.xiaohunao.heaven_destiny_moment.common.terra_moment.moment.instance.BloodMoonInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class BloodMoonMoment extends Moment {
    public BloodMoonMoment(ResourceLocation barRenderType, MomentDataContext momentDataContext, ClientSettingsContext clientSettingsContext) {
        super(barRenderType, momentDataContext, clientSettingsContext);
    }

    @Override
    public MomentInstance newMomentInstance(Level level) {
        return new BloodMoonInstance(level,this);
    }
}
