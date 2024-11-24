package com.xiaohunao.heaven_destiny_moment.common.moment.moment.instance;

import com.xiaohunao.heaven_destiny_moment.common.init.MomentTypes;
import com.xiaohunao.heaven_destiny_moment.common.moment.Moment;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class DefaultInstance extends MomentInstance {
    public DefaultInstance(Level level, ResourceKey<Moment> moment) {
        super(MomentTypes.DEFAULT.get(), level, moment);
    }

    public DefaultInstance(UUID uuid, Level level, ResourceKey<Moment> moment) {
        super( MomentTypes.DEFAULT.get(), uuid, level, moment);
    }

    @Override
    public void finalizeSpawn(Entity entity) {

    }
}
