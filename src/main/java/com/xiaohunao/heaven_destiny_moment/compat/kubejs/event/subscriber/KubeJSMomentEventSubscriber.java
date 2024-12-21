package com.xiaohunao.heaven_destiny_moment.compat.kubejs.event.subscriber;

import com.xiaohunao.heaven_destiny_moment.common.event.MomentEvent;
import com.xiaohunao.heaven_destiny_moment.common.event.PlayerMomentAreaEvent;
import com.xiaohunao.heaven_destiny_moment.common.moment.MomentState;
import com.xiaohunao.heaven_destiny_moment.compat.kubejs.event.HDMMomentKubeJSEvents;
import com.xiaohunao.heaven_destiny_moment.compat.kubejs.event.MomentStateEventJS;
import com.xiaohunao.heaven_destiny_moment.compat.kubejs.event.MomentTickEventJS;
import net.neoforged.bus.api.SubscribeEvent;


public class KubeJSMomentEventSubscriber {
    @SubscribeEvent
    public static void onMomentReady(MomentEvent.Ready event) {
        if (HDMMomentKubeJSEvents.STATE.hasListeners(MomentState.READY)){
            HDMMomentKubeJSEvents.STATE.post(new MomentStateEventJS(event.getMomentInstance()),MomentState.READY).applyCancel(event);
        }
    }

    @SubscribeEvent
    public static void onMomentStart(MomentEvent.Start event) {
        if (HDMMomentKubeJSEvents.STATE.hasListeners(MomentState.START)){
            HDMMomentKubeJSEvents.STATE.post(new MomentStateEventJS(event.getMomentInstance()),MomentState.START).applyCancel(event);
        }
    }

    @SubscribeEvent
    public static void onMomentOnGoing(MomentEvent.OnGoing event) {
        HDMMomentKubeJSEvents.STATE.post(new MomentStateEventJS(event.getMomentInstance()),MomentState.ONGOING);
    }

    @SubscribeEvent
    public static void onMomentVictory(MomentEvent.Victory event) {
        if (HDMMomentKubeJSEvents.STATE.hasListeners(MomentState.VICTORY)){
            HDMMomentKubeJSEvents.STATE.post(new MomentStateEventJS(event.getMomentInstance()),MomentState.VICTORY).applyCancel(event);
        }
    }


    @SubscribeEvent
    public static void onMomentLose(MomentEvent.Lose event) {
        if (HDMMomentKubeJSEvents.STATE.hasListeners(MomentState.LOSE)) {
            HDMMomentKubeJSEvents.STATE.post(new MomentStateEventJS(event.getMomentInstance()), MomentState.LOSE).applyCancel(event);
        }
    }

    @SubscribeEvent
    public static void onMomentEnd(MomentEvent.End event) {
        if (HDMMomentKubeJSEvents.STATE.hasListeners(MomentState.END)) {
            HDMMomentKubeJSEvents.STATE.post(new MomentStateEventJS(event.getMomentInstance()), MomentState.END);
        }
    }

    @SubscribeEvent
    public static void onMomentTick(MomentEvent.Tick event) {
        if (HDMMomentKubeJSEvents.Tick.hasListeners()) {
            HDMMomentKubeJSEvents.Tick.post(new MomentTickEventJS(event.getMomentInstance()));
        }
    }


    @SubscribeEvent
    public static void onPlayerMomentAreaEnter(PlayerMomentAreaEvent.Enter event) {

    }
    @SubscribeEvent
    public static void onPlayerMomentAreaExit(PlayerMomentAreaEvent.Exit event) {

    }
}
