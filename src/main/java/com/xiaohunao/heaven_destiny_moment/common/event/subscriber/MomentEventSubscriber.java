package com.xiaohunao.heaven_destiny_moment.common.event.subscriber;

import com.xiaohunao.heaven_destiny_moment.common.event.MomentEvent;
import com.xiaohunao.heaven_destiny_moment.common.event.PlayerMomentAreaEvent;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber
public class MomentEventSubscriber {
    @SubscribeEvent
    public static void onMomentReady(MomentEvent.Ready event) {

    }

    @SubscribeEvent
    public static void onMomentStart(MomentEvent.Start event) {

    }

    @SubscribeEvent
    public static void onMomentOnGoing(MomentEvent.OnGoing event) {
    }

    @SubscribeEvent
    public static void onMomentVictory(MomentEvent.Victory event) {

    }


    @SubscribeEvent
    public static void onMomentLose(MomentEvent.Lose event) {

    }

    @SubscribeEvent
    public static void onMomentEnd(MomentEvent.End event) {

    }

    @SubscribeEvent
    public static void onPlayerMomentAreaEnter(PlayerMomentAreaEvent.Enter event) {

    }
    @SubscribeEvent
    public static void onPlayerMomentAreaExit(PlayerMomentAreaEvent.Exit event) {

    }
}