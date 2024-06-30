package org.soloquest.soloscan.runtime.lang;

import java.math.BigInteger;
import java.util.Map;

public class SBigInt extends SNumber {

    public SBigInt(Number number) {
        super(number);
    }

    public static final SBigInt valueOf(BigInteger v) {
        return new SBigInt(v);
    }

    public static final SBigInt valueOf(String v) {
        return new SBigInt(new BigInteger(v));
    }

    public static final SBigInt valueOf(long l) {
        final int offset = 128;
        if (l >= -128 && l <= 127) {
            return BigIntCache.cache[(int) l + offset];
        }
        return valueOf(BigInteger.valueOf(l));
    }

    @Override
    public Number getValue(Map<String, Object> env) {
        return this.number;
    }

    @Override
    public SObjectType getSObjectType() {
        return SObjectType.BigInt;
    }

    private static class BigIntCache {
        static final SBigInt cache[] = new SBigInt[256];

        static {
            for (long i = 0; i < cache.length; i++) {
                cache[(int) i] = new SBigInt(BigInteger.valueOf(i - 128));
            }
        }

        private BigIntCache() {
        }
    }

}
