package com.xiaohunao.heaven_destiny_moment.common.event;


import com.xiaohunao.heaven_destiny_moment.common.moment.MomentManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber
public class LevelEventSubscriber {
    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        Level level = event.getLevel();
        if (!level.isClientSide){
            MomentManager.of((ServerLevel) level).tick();
        }
    }
}
