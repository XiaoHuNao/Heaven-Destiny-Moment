package com.xiaohunao.heaven_destiny_moment.common.context;

import net.minecraft.world.entity.EntityType;

import java.util.Map;

public class EntityBattlePointContext {
    public static EntityBattlePointContext CONTEXT = new EntityBattlePointContext();
    private Map<EntityType<?>,Integer> entityPoint;

    private EntityBattlePointContext() {
    }

    public void register(EntityType<?> entityType, int point) {
        entityPoint.put(entityType, point);
    }
    public int getPoint(EntityType<?> entityType) {
        return entityPoint.getOrDefault(entityType, 1);
    }
}
