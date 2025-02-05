package com.xiaohunao.heaven_destiny_moment.common.event;

import com.xiaohunao.heaven_destiny_moment.common.callback.CallbackManager;
import com.xiaohunao.heaven_destiny_moment.common.callback.CallbackSerializable;
import net.neoforged.bus.api.Event;

import java.util.Map;

/**
 * 回调注册事件
 * 用于注册回调接口和原始类型映射
 */
public class RegisterCallbackEvent extends Event {
    private final CallbackRegistry callbackRegistry;
    private final PrimitiveTypeRegistry primitiveTypeRegistry;

    public RegisterCallbackEvent() {
        this.callbackRegistry = new CallbackRegistry();
        this.primitiveTypeRegistry = new PrimitiveTypeRegistry();
    }

    public CallbackRegistry getCallbackRegistry() {
        return callbackRegistry;
    }

    public PrimitiveTypeRegistry getPrimitiveTypeRegistry() {
        return primitiveTypeRegistry;
    }

    public static class CallbackRegistry {
        @SafeVarargs
        public final void register(Class<? extends CallbackSerializable>... callbacks) {
            for (Class<? extends CallbackSerializable> callback : callbacks) {
                CallbackManager.registerCallback(callback);
            }
        }
    }

    public static class PrimitiveTypeRegistry {
        public void register(String typeName, Class<?> typeClass) {
            CallbackManager.registerPrimitiveType(typeName, typeClass);
        }

        public void registerAll(Map<String, Class<?>> mappings) {
            for (Map.Entry<String, Class<?>> entry : mappings.entrySet()) {
                register(entry.getKey(), entry.getValue());
            }
        }
    }
} 