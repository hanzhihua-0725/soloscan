package org.soloquest.soloscan.runtime.lang;

import org.soloquest.soloscan.exception.ExpressionExecuteException;
import org.soloquest.soloscan.utils.Env;
import org.soloquest.soloscan.utils.TypeUtils;

import java.util.Map;

public abstract class SObject<T> {


    public SObject add(final SObject other, final Map<String, Object> env) {
        return SNumber.valueOf(Numbers.add(getValue(env), other.getValue(env)));
    }

    public SObject sub(final SObject other, final Map<String, Object> env) {
        return SNumber.valueOf(Numbers.minus(getValue(env), other.getValue(env)));
    }


    public SObject mod(final SObject other, final Map<String, Object> env) {
        return SNumber.valueOf(Numbers.remainder(getValue(env), other.getValue(env)));
    }


    public SObject div(final SObject other, final Map<String, Object> env) {
        return SNumber.valueOf(Numbers.divide(getValue(env), other.getValue(env)));
    }


    public SObject mult(final SObject other, final Map<String, Object> env) {
        return SNumber.valueOf(Numbers.multiply(getValue(env), other.getValue(env)));
    }

    public SObject union(final SObject other, final Map<String, Object> env) {
        throw new ExpressionExecuteException(desc(env) + " doesn't support not operation '!'");
    }

    public SObject defaultOperation(final SObject other, final Map<String, Object> env) {
        throw new ExpressionExecuteException(desc(env) + " doesn't support not operation '!'");
    }

    public SObject neg(final Map<String, Object> env) {
        return SNumber.valueOf(Numbers.ops(getValue(env)).negate((Number) getValue(env)));
    }

    public SObject not(final Map<String, Object> env) {
        throw new ExpressionExecuteException(desc(env) + " doesn't support not operation '!'");
    }

    public int compare(final SObject other, final Map<String, Object> env) {
        if (this == other) {
            return 0;
        }
        return innerCompare(other, env);
    }

    public int innerCompare(SObject other, Map<String, Object> env) {
        Object thisValue = getValue(env);
        Object otherValue = other.getValue(env);
        if (thisValue == otherValue) {
            return 0;
        } else if (thisValue == null) {
            return -1;
        } else if (otherValue == null) {
            return 1;
        }
        return Numbers.compare((Number) thisValue, (Number) otherValue);
    }


    public SObject in(final SObject other, final Map<String, Object> env) {
        throw new ExpressionExecuteException(
                "Could not in " + desc(env) + " with " + other.desc(env));
    }


    public abstract SObjectType getSObjectType();


    @Override
    public String toString() {
        return desc(Env.EMPTY_ENV);
    }


    public String desc(final Map<String, Object> env) {
        Object val = getValue(env);
        if (val != this) {
            return "<" + getSObjectType() + ", " + val + ">";
        } else {
            return "<" + getSObjectType() + ", this>";
        }
    }

    public abstract T getValue(Map<String, Object> env);

    public Number numberValue(final Map<String, Object> env) {
        if (!(getValue(env) instanceof Number)) {
            throw new ExpressionExecuteException(desc(env) + " is not a number value");
        }
        return (Number) getValue(env);
    }


    public String stringValue(final Map<String, Object> env) {
        Object value = getValue(env);
        if (!(TypeUtils.isString(value))) {
            throw new ExpressionExecuteException(desc(env) + " is not a string value");
        }
        return String.valueOf(value);
    }


    public boolean booleanValue(final Map<String, Object> env) {
        final Object val = getValue(env);
        if (val instanceof Boolean) {
            return (boolean) val;
        } else if (val instanceof Integer) {
            return ((Integer) val).intValue() != 0;
        } else if (val instanceof Long) {
            return ((Long) val).longValue() != 0;
        } else {
            return val != null;
        }
    }

}
