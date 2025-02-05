package com.xiaohunao.heaven_destiny_moment.common.callback;

import com.xiaohunao.heaven_destiny_moment.HeavenDestinyMoment;
import com.xiaohunao.heaven_destiny_moment.common.callback.callback.ConditionCallback;
import com.xiaohunao.heaven_destiny_moment.common.callback.callback.RewardCallback;
import com.xiaohunao.heaven_destiny_moment.common.event.RegisterCallbackEvent;
import com.xiaohunao.heaven_destiny_moment.compat.kubejs.builder.MomentTypeKubeJSBuilder;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 回调函数管理器
 * 负责管理所有回调接口的注册和事件分发
 */
public class CallbackManager {
    private static final Map<CallbackMetadata.CallbackSignature, Class<?>> CALLBACK_INTERFACES = new HashMap<>();
    private static final Map<String, Class<?>> PRIMITIVE_TYPE_MAP = new HashMap<>();
    
    /**
     * 初始化回调管理器
     * @param modEventBus mod事件总线
     */
    public static void init(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.addListener(CallbackManager::onRegisterCallbacks);
    }

    /**
     * 解析类名为Class对象
     */
    public static Class<?> resolveClass(String className) {
        try {
            if (PRIMITIVE_TYPE_MAP.containsKey(className)) {
                return PRIMITIVE_TYPE_MAP.get(className);
            }
            if (className.startsWith("[")) {
                return Class.forName(className);
            }
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to resolve class: " + className, e);
        }
    }

    /**
     * 注册回调接口
     * @param callbackInterface 要注册的回调接口类
     */
    public static void registerCallback(Class<? extends CallbackSerializable> callbackInterface) {
        validateCallbackInterface(callbackInterface);
        Method functionalMethod = getFunctionalInterfaceMethod(callbackInterface);
        String returnTypeName = functionalMethod.getReturnType().getName();
        Class<?>[] methodParamTypes = functionalMethod.getParameterTypes();

        CallbackMetadata.CallbackSignature signature = 
            new CallbackMetadata.CallbackSignature(returnTypeName, methodParamTypes);
        CALLBACK_INTERFACES.put(signature, callbackInterface);
    }

    public static void registerPrimitiveType(String typeName, Class<?> typeClass) {
        PRIMITIVE_TYPE_MAP.put(typeName, typeClass);
    }


    /**
     * 根据方法签名查找对应的回调接口
     */
    public static Class<?> findCallbackInterface(String returnType, Class<?>[] paramTypes) {
        return CALLBACK_INTERFACES.entrySet().stream()
            .filter(entry -> entry.getKey().matches(returnType, paramTypes))
            .map(Map.Entry::getValue)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                "No matching callback interface found for signature: " +
                java.util.Arrays.toString(paramTypes) + " -> " + returnType
            ));
    }

    /**
     * 检查接口是否已注册
     */
    public static boolean isCallbackInterfaceRegistered(Class<?> callbackInterface) {
        Method method = getFunctionalInterfaceMethod(callbackInterface);
        String returnType = method.getReturnType().getName();
        Class<?>[] paramTypes = method.getParameterTypes();
        CallbackMetadata.CallbackSignature signature = 
            new CallbackMetadata.CallbackSignature(returnType, paramTypes);
        return CALLBACK_INTERFACES.containsKey(signature);
    }

    private static Method getFunctionalInterfaceMethod(Class<?> interfaceClass) {
        if (!interfaceClass.isAnnotationPresent(FunctionalInterface.class)) {
            throw new IllegalArgumentException(
                "Interface must be annotated with @FunctionalInterface: " + interfaceClass
            );
        }

        return java.util.Arrays.stream(interfaceClass.getMethods())
                .filter(method -> !method.isDefault() && !java.lang.reflect.Modifier.isStatic(method.getModifiers()))
                .findFirst()
                .orElse(null);
    }

    private static void validateCallbackInterface(Class<?> callbackInterface) {
        if (!callbackInterface.isInterface()) {
            throw new IllegalArgumentException("Callback type must be an interface: " + callbackInterface);
        }
    }


    private static void onRegisterCallbacks(RegisterCallbackEvent event) {
        // 注册默认的回调接口
        event.getCallbackRegistry().register(
            ConditionCallback.class,
            RewardCallback.class,
            MomentTypeKubeJSBuilder.CanSpawnEntityCallback.class,
            MomentTypeKubeJSBuilder.CanCreateCallback.class
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