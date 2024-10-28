package com.xiaohunao.heaven_destiny_moment.common.event.subscriber;

import com.xiaohunao.heaven_destiny_moment.common.event.MomentEvent;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentInstance;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber
public class MomentEventSubscriber {
    @SubscribeEvent
    public static void onMomentReady(MomentEvent.Ready event) {
        MomentInstance momentInstance = event.getMomentInstance();
//        momentInstance.playTip();
    }

    @SubscribeEvent
    public static void onMomentStart(MomentEvent.Start event) {
        MomentInstance momentInstance = event.getMomentInstance();
//        momentInstance.playTip();
    }

    @SubscribeEvent
    public static void onMomentOnGoing(MomentEvent.OnGoing event) {
        MomentInstance momentInstance = event.getMomentInstance();
//        momentInstance.playTip();
    }

    @SubscribeEvent
    public static void onMomentCelebrating(MomentEvent.Celebrating event) {
        MomentInstance momentInstance = event.getMomentInstance();
//        momentInstance.playTip();
    }

    @SubscribeEvent
    public static void onMomentVictory(MomentEvent.Victory event) {
        MomentInstance momentInstance = event.getMomentInstance();
//        momentInstance.playTip();
    }


    @SubscribeEvent
    public static void onMomentLose(MomentEvent.Lose event) {
        MomentInstance momentInstance = event.getMomentInstance();
//        momentInstance.playTip();
    }

    @SubscribeEvent
    public static void onMomentEnd(MomentEvent.End event) {
        MomentInstance momentInstance = event.getMomentInstance();
//        momentInstance.playTip();
    }
}