package com.xiaohunao.heaven_destiny_moment.common.event;

import com.xiaohunao.heaven_destiny_moment.common.context.EntityBattlePointContext;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.eventbus.api.Event;

public class EntityBattlePointRegisterEvent extends Event {
    private final EntityBattlePointContext context = EntityBattlePointContext.CONTEXT;

    public void register(EntityType<?> entityType, int battlePoint) {
        context.register(entityType,battlePoint);
    }
}
