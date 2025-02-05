package com.xiaohunao.heaven_destiny_moment.common.callback;

import java.lang.reflect.Method;
import java.util.Arrays;
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


    private static Method getFunctionalInterfaceMethod(Class<?> interfaceClass) {
        if (!interfaceClass.isAnnotationPresent(FunctionalInterface.class)) {
            throw new IllegalArgumentException(
                "Interface must be annotated with @FunctionalInterface: " + interfaceClass
            );
        }

        return Arrays.stream(interfaceClass.getMethods())
                .filter(method -> !method.isDefault() && !java.lang.reflect.Modifier.isStatic(method.getModifiers()))
                .findFirst()
                .orElse(null);
    }

    private static void validateCallbackInterface(Class<?> callbackInterface) {
        if (!callbackInterface.isInterface()) {
            throw new IllegalArgumentException("Callback type must be an interface: " + callbackInterface);
        }
    }
}