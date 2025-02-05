package com.xiaohunao.heaven_destiny_moment.common.event.subscriber;

import com.xiaohunao.heaven_destiny_moment.common.callback.callback.ConditionCallback;
import com.xiaohunao.heaven_destiny_moment.common.callback.callback.RewardCallback;
import com.xiaohunao.heaven_destiny_moment.common.event.RegisterCallbackEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.Map;

@EventBusSubscriber
public class RegisterCallbackEventSubscriber {
    @SubscribeEvent
    public static void onRegisterCallbacks(RegisterCallbackEvent event) {
        event.getCallbackRegistry().register(
                ConditionCallback.class,
                RewardCallback.class
        );


        event.getPrimitiveTypeRegistry().registerAll(Map.of(
                "boolean", boolean.class,
                "byte", byte.class,
                "char", char.class,
                "short", short.class,
                "int", int.class,
                "long", long.class,
                "float", float.class,
                "double", double.class,
                "void", void.class
        ));
    }
}
