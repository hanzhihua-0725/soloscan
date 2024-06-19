package org.soloquest.soloscan.runtime.lang;

public class SLong extends SNumber {


    private static class LongCache {
        private LongCache() {
        }

        static final SLong cache[] = new SLong[256];

        static {
            for (long i = 0; i < cache.length; i++) {
                cache[(int) i] = new SLong(i - 128);
            }
        }
    }

    SLong(final long l) {
        super(l);

    }


    public static SLong valueOf(final long l) {
        final int offset = 128;
        if (l >= -128 && l <= 127) { // will cache
            return LongCache.cache[(int) l + offset];
        }
        return new SLong(l);
    }

    @Override
    public SObjectType getSObjectType() {
        return SObjectType.Long;
    }

}
