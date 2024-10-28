package com.xiaohunao.heaven_destiny_moment.common.event.subscriber;


import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;


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

//        Moment.CODEC.encodeStart(JsonOps.INSTANCE, ModMoment.BLOOD_MOON.get()).result().ifPresent(jsonElement -> {
//            System.out.println(jsonElement);
//        });
//        if (level instanceof ServerLevel serverLevel) {
//            MomentInstance.create(serverLevel, ModMoment.BLOOD_MOON.get());
//        }
    }

}