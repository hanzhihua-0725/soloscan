package org.soloquest.soloscan.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class TypeUtils {

    public static final boolean isBigInt(final Object value) {
        return value instanceof BigInteger;
    }


    public static final boolean isDecimal(final Object value) {
        return value instanceof BigDecimal;
    }


    public static final boolean isLong(final Object value) {
        return value instanceof Integer || value instanceof Long || value instanceof Byte
                || value instanceof Short;
    }


    public static final boolean isDouble(final Object value) {
        return value instanceof Float || value instanceof Double;

    }


    public static final boolean isString(final Object value) {
        return value instanceof String || value instanceof Character;
    }

    public static long NEWTON_METHOD_REPEATS = 10000;


    public static int comapreLong(final long x, final long y) {
        if (x > y) {
            return 1;
        } else if (x < y) {
            return -1;
        } else {
            return 0;
        }
    }


    public static final Map<String, Class<?>> PRIMITIVE_TYPES = new HashMap<>();


    static {
        TypeUtils.PRIMITIVE_TYPES.put("int", Integer.TYPE);
        TypeUtils.PRIMITIVE_TYPES.put("long", Long.TYPE);
        TypeUtils.PRIMITIVE_TYPES.put("double", Double.TYPE);
        TypeUtils.PRIMITIVE_TYPES.put("float", Float.TYPE);
        TypeUtils.PRIMITIVE_TYPES.put("bool", Boolean.TYPE);
        TypeUtils.PRIMITIVE_TYPES.put("char", Character.TYPE);
        TypeUtils.PRIMITIVE_TYPES.put("byte", Byte.TYPE);
        TypeUtils.PRIMITIVE_TYPES.put("void", Void.TYPE);
        TypeUtils.PRIMITIVE_TYPES.put("short", Short.TYPE);
    }

}
