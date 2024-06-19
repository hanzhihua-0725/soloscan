package org.soloquest.soloscan.compiler.codegen;

import lombok.extern.slf4j.Slf4j;
import org.soloquest.soloscan.SoloscanOptions;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

import static java.lang.invoke.MethodType.methodType;

@Slf4j
public class ClassDefiner {
    private static final Object[] EMPTY_OBJS = new Object[]{};
    private static MethodHandle DEFINE_CLASS_HANDLE;

    static {
        try {
            Class<?> clazz = Class.forName("sun.misc.Unsafe");
            if (clazz != null) {
                Field f = clazz.getDeclaredField("theUnsafe");
                f.setAccessible(true);
                Object unsafe = f.get(null);
                MethodHandle methodHandle =
                        MethodHandles.lookup().findVirtual(clazz, "defineAnonymousClass",
                                methodType(Class.class, Class.class, byte[].class, Object[].class));

                if (methodHandle != null) {
                    methodHandle = methodHandle.bindTo(unsafe);
                }
                DEFINE_CLASS_HANDLE = methodHandle;
            }
        } catch (Throwable e) {
            log.warn("Failed to get methodhandle", e);
        }
    }

    private static boolean userMethodHandle = SoloscanOptions.getOption(SoloscanOptions.USE_METHODHANDLE);

    public static final Class<?> defineClass(final String className, final Class<?> clazz,
                                             final byte[] bytes, final SoloscanClassloader classLoader) throws Throwable {
        if (userMethodHandle && DEFINE_CLASS_HANDLE != null) {
            return (Class<?>) DEFINE_CLASS_HANDLE.invokeExact(clazz, bytes, EMPTY_OBJS);
        } else {
            return defineClassByClassLoader(className, bytes, classLoader);
        }
    }

    public static Class<?> defineClassByClassLoader(final String className, final byte[] bytes,
                                                    final SoloscanClassloader classLoader) {
        return classLoader.defineClass(className, bytes);
    }
}
