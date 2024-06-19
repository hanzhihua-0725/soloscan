package org.soloquest.soloscan.runtime.lang;

import org.soloquest.soloscan.SoloscanExecutor;
import org.soloquest.soloscan.utils.Env;

import java.math.MathContext;
import java.util.Map;

public class RuntimeUtils {

    private RuntimeUtils() {

    }

    public static final SoloscanExecutor getInstance(final Map<String, Object> env) {
        if (env instanceof Env) {
            return ((Env) env).getInstance();
        }
        return null;

    }

    public static final SFunction getFunction(String functionName){
        return SoloscanExecutor.INSTANCE.getFunction(functionName);
    }


    public static final SObject assertNotNull(final SObject object) {
        if (object != null) {
            return object;
        }
        return SNull.NULL;
    }

    public static final MathContext getMathContext(final Map<String, Object> env) {
        return null;
    }


    public static final boolean isTracedEval(final Map<String, Object> env) {
        return false;
    }

    public static SFunction getFunction(final Object object, final Map<String, Object> env) {
        if (object instanceof SFunction) {
            return (SFunction) object;
        } else if (object instanceof SObject) {
            Object value = ((SObject) object).getValue(env);
            if (value instanceof SFunction) {
                return (SFunction) value;
            }
        }
        throw new ClassCastException("Could not cast object " + object + " into a yaparser function.");
    }

    public static SFunction getFunction(final Map<String, Object> env, final String name) {
        return getInstance(env).getFunction(name);
    }

    public static void printStackTrace(final Map<String, Object> env, final Exception e) {
        if (isTracedEval(env)) {
            e.printStackTrace();
        }
    }

    public static boolean covertBoolean(final Object object) {
        if (object == null) {
            return false;
        }
        if (object == boolean.class) {
            return (boolean) object;
        }
        if (object instanceof Boolean) {
            return ((Boolean) object).booleanValue();
        }
//        if (object instanceof Number) {
//            return ((Number) object).intValue() != 0;
//        }
        return true;
    }

    public static String covertString(final Object object) {
        if (object == null) {
            return "NO_ANSWER";
        }
        return object.toString();
    }


}
