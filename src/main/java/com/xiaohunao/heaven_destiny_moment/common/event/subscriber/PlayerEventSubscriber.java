package com.xiaohunao.heaven_destiny_moment.common.event.subscriber;

import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.init.ModMoments;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, modid = HeavenDestinyMoment.MODID)
public class PlayerEventSubscriber {
    @SubscribeEvent
    public static void onPlayerInteractRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        InteractionHand hand = event.getHand();
        Player player = event.getEntity();
        if (hand != InteractionHand.MAIN_HAND) return;

        if (level instanceof ServerLevel serverLevel) {
            MomentInstance.create(ModMoments.BLOOD_MOON, serverLevel);
//            Registry<Moment> moments = serverLevel.registryAccess().registryOrThrow(MomentRegistries.Keys.MOMENT);
//            System.out.println(moments);
        } else {
//            Registry<Moment> moments = clientLevel.registryAccess().registryOrThrow(MomentRegistries.Keys.MOMENT);
//            System.out.println(moments);
        }
    }
}