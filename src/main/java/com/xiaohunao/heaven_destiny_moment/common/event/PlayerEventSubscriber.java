package com.xiaohunao.heaven_destiny_moment.common.event;

import com.xiaohunao.heaven_destiny_moment.common.init.MomentRegistry;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;


@EventBusSubscriber
public class PlayerEventSubscriber {
    @SubscribeEvent
    public static void onPlayerInteractRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        InteractionHand hand = event.getHand();
        Player player = event.getEntity();
        if (level.isClientSide() || hand != InteractionHand.MAIN_HAND) {
            return;
        }

        if (level instanceof ServerLevel serverLevel) {
            MomentManager momentManager = MomentManager.of(serverLevel);
            System.out.println(momentManager);
        }
    }

}