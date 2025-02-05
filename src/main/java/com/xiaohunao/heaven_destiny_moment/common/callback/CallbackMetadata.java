package com.xiaohunao.heaven_destiny_moment.common.callback;

import com.google.gson.Gson;
import com.mojang.serialization.Codec;
import org.objectweb.asm.Type;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.*;
import java.util.Arrays;

/**
 * 回调函数元数据类，用于序列化和反序列化函数式接口
 * 支持自动注册和管理不同类型的回调接口
 */
public record CallbackMetadata(String implClass, String implMethodName, String[] paramTypes, String returnType) {
    private static final Gson gson = new Gson();
    public static final Codec<CallbackMetadata> CODEC = Codec.STRING.xmap(
        jsonStr -> gson.fromJson(jsonStr, CallbackMetadata.class), 
        gson::toJson
    );

    
    /**
     * 回调函数签名记录类，用于标识不同类型的回调函数
     */
    public record CallbackSignature(String returnType, Class<?>... paramTypes) {
        /**
         * 检查方法签名是否匹配
         * @param targetReturnType 目标返回类型
         * @param targetParamTypes 目标参数类型数组
         * @return 是否匹配
         */
        public boolean matches(String targetReturnType, Class<?>[] targetParamTypes) {
            if (!this.returnType.equals(targetReturnType) || 
                this.paramTypes.length != targetParamTypes.length) {
                return false;
            }
            for (int i = 0; i < targetParamTypes.length; i++) {
                if (!this.paramTypes[i].isAssignableFrom(targetParamTypes[i])) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * 序列化回调函数为元数据
     * @param callback 要序列化的回调函数
     * @return 回调函数元数据
     */
    public static CallbackMetadata serialize(CallbackSerializable callback) {
        try {
            Method serializationMethod = callback.getClass().getDeclaredMethod("writeReplace");
            serializationMethod.setAccessible(true);
            SerializedLambda lambdaInfo = (SerializedLambda) serializationMethod.invoke(callback);

            String[] methodParamTypes = parseMethodSignature(lambdaInfo.getImplMethodSignature());
            String methodReturnType = parseReturnType(lambdaInfo.getImplMethodSignature());

            return new CallbackMetadata(
                    lambdaInfo.getImplClass().replace("/", "."),
                    lambdaInfo.getImplMethodName(),
                    methodParamTypes,
                    methodReturnType
            );
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 反序列化元数据为回调函数
     * @param metadata 回调函数元数据
     * @return 反序列化后的回调函数
     */
    public static CallbackSerializable deserialize(CallbackMetadata metadata){
        try {
            Class<?> implementationClass = Class.forName(metadata.implClass());
            Class<?>[] parameterTypes = Arrays.stream(metadata.paramTypes)
                    .map(CallbackMetadata::resolveClass)
                    .toArray(Class<?>[]::new);

            Method implementationMethod = implementationClass.getDeclaredMethod(
                    metadata.implMethodName,
                    parameterTypes
            );
            implementationMethod.setAccessible(true);

            Class<?> callbackType = CallbackManager.findCallbackInterface(metadata.returnType, parameterTypes);

            return (CallbackSerializable) Proxy.newProxyInstance(
                    CallbackMetadata.class.getClassLoader(),
                    new Class<?>[] { callbackType },
                    new CallbackInvocationHandler(implementationMethod, metadata)
            );
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
         * 回调函数代理处理器
         */
        private record CallbackInvocationHandler(Method targetMethod, CallbackMetadata metadataInfo) implements InvocationHandler {

        @Override
            public Object invoke(Object proxy, Method proxyMethod, Object[] args) throws Throwable {
                if (proxyMethod.getDeclaringClass() != Object.class) {
                    return targetMethod.invoke(null, args);
                }

                return switch (proxyMethod.getName()) {
                    case "equals" -> args.length == 1 && proxy == args[0];
                    case "hashCode" -> System.identityHashCode(proxy);
                    case "toString" -> proxyMethod.getDeclaringClass().getSimpleName() +
                            "Proxy[" + metadataInfo.implClass() + "#" +
                            metadataInfo.implMethodName() + "]";
                    default -> proxyMethod.invoke(proxy, args);
                };
            }
        }

    private static Method getFunctionalInterfaceMethod(Class<?> interfaceClass) {
        if (!interfaceClass.isAnnotationPresent(FunctionalInterface.class)) {
            throw new IllegalArgumentException("Interface must be annotated with @FunctionalInterface: " + interfaceClass);
        }

        return Arrays.stream(interfaceClass.getMethods())
                .filter(method -> !method.isDefault() && !Modifier.isStatic(method.getModifiers()))
                .findFirst()
                .orElse(null);
    }

    private static Class<?> findCallbackInterface(String returnType, Class<?>[] paramTypes) {
        return CallbackManager.findCallbackInterface(returnType, paramTypes);
    }

    private static Class<?> resolveClass(String className) {
        return CallbackManager.resolveClass(className);
    }

    private static String[] parseMethodSignature(String signature) {
        try {
            Type methodType = org.objectweb.asm.Type.getMethodType(signature);
            Type[] argumentTypes = methodType.getArgumentTypes();
            
            return Arrays.stream(argumentTypes)
                    .map(Type::getClassName)
                    .toArray(String[]::new);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse method signature", e);
        }
    }

    private static String parseReturnType(String signature) {
        try {
            Type methodType = Type.getMethodType(signature);
            return methodType.getReturnType().getClassName();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse return type", e);
        }
    }
}
