package com.xiaohunao.heaven_destiny_moment.common.event.subscriber;

import com.xiaohunao.heaven_destiny_moment.common.capability.MomentCap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class LevelEventSubscriber {

    @SubscribeEvent
    public static void onTickLevelTick(TickEvent.LevelTickEvent event) {
        MomentCap cap = MomentCap.getCap(event.level);
        cap.tick();
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        MomentCap cap = MomentCap.getCap(entity.level());
        cap.addMomentKillCount(entity);
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (player.level() instanceof ServerLevel){
            MomentCap.getCap(player.level()).sync();
        }

    }


}
