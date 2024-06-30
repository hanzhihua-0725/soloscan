package org.soloquest.soloscan.runtime.lang;

import org.soloquest.soloscan.utils.TypeUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

public abstract class SNumber extends SObject<Number> {

    protected Number number;


    public SNumber(final Number number) {
        super();
        this.number = number;
    }

    public static SNumber valueOf(final Object value) {
        if (TypeUtils.isLong(value)) {
            return SLong.valueOf(((Number) value).longValue());
        } else if (TypeUtils.isDouble(value)) {
            return new SDouble(((Number) value).doubleValue());
        } else if (TypeUtils.isBigInt(value)) {
            return SBigInt.valueOf((BigInteger) value);
        } else if (TypeUtils.isDecimal(value)) {
            return SDecimal.valueOf((BigDecimal) value);
        } else {
            throw new ClassCastException("Could not cast " + value.getClass().getName() + " to Number");
        }

    }

    @Override
    public Number getValue(final Map<String, Object> env) {
        return this.number;
    }

    public double doubleValue() {
        return this.number.doubleValue();
    }


    @Override
    public SObject add(final SObject other, final Map<String, Object> env) {
        switch (other.getSObjectType()) {
            case String:
                return new SString(getValue(env).toString() + other.getValue(env));
            default:
                return super.add(other, env);
        }

    }

    public long longValue() {
        return this.number.longValue();
    }

}
