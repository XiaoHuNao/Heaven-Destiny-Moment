package com.xiaohunao.heaven_destiny_moment.common.moment.moment.instance;

import com.xiaohunao.heaven_destiny_moment.common.init.HDMMomentTypes;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import com.xiaohunao.heaven_destiny_moment.common.moment.moment.DefaultMoment;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class DefaultInstance extends MomentInstance<DefaultMoment> {
    public DefaultInstance(Level level, ResourceKey<Moment> moment) {
        super(HDMMomentTypes.DEFAULT.get(), level, moment);
    }

    public DefaultInstance(UUID uuid, Level level, ResourceKey<Moment> moment) {
        super( HDMMomentTypes.DEFAULT.get(), uuid, level, moment);
    }
}
