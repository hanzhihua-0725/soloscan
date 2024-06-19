package org.soloquest.soloscan.runtime.lang;

import java.math.BigInteger;

public class RT {

    static public long longCast(Object x) {
        if (x instanceof Integer || x instanceof Long)
            return ((Number) x).longValue();
        else if (x instanceof BigInt) {
            BigInt bi = (BigInt) x;
            if (bi.bipart == null)
                return bi.lpart;
            else
                throw new IllegalArgumentException("Value out of range for long: " + x);
        } else if (x instanceof BigInteger) {
            BigInteger bi = (BigInteger) x;
            if (bi.bitLength() < 64)
                return bi.longValue();
            else
                throw new IllegalArgumentException("Value out of range for long: " + x);
        } else if (x instanceof Byte || x instanceof Short)
            return ((Number) x).longValue();
        else if (x instanceof Ratio)
            return longCast(((Ratio) x).bigIntegerValue());
        else if (x instanceof Character)
            return longCast(((Character) x).charValue());
        else
            return longCast(((Number) x).doubleValue());
    }

    static public long longCast(byte x) {
        return x;
    }

    static public long longCast(short x) {
        return x;
    }

    static public long longCast(int x) {
        return x;
    }

    static public long longCast(float x) {
        if (x < Long.MIN_VALUE || x > Long.MAX_VALUE)
            throw new IllegalArgumentException("Value out of range for long: " + x);
        return (long) x;
    }

    static public long longCast(long x) {
        return x;
    }

    static public long longCast(double x) {
        if (x < Long.MIN_VALUE || x > Long.MAX_VALUE)
            throw new IllegalArgumentException("Value out of range for long: " + x);
        return (long) x;
    }
}
