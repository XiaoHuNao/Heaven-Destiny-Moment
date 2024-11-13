package com.xiaohunao.heaven_destiny_moment.common.moment.moment.instance;

import com.xiaohunao.heaven_destiny_moment.common.init.ModMomentTypes;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class DefaultInstance extends MomentInstance {
    public DefaultInstance(Level level, ResourceKey<Moment> moment) {
        super(ModMomentTypes.DEFAULT.get(), level, moment);
    }

    @Override
    public void finalizeSpawn(LivingEntity livingEntity) {

    }
}
