package org.soloquest.soloscan.runtime.lang;

import org.soloquest.soloscan.SoloscanExecutor;

import java.math.BigDecimal;
import java.util.Map;

public class SDecimal extends SNumber {
    public SDecimal(final Number number) {
        super(number);
    }


    public static final SDecimal valueOf(final BigDecimal d) {
        return new SDecimal(d);
    }


    public static final SDecimal valueOf(final Map<String, Object> env, final String d) {
        return new SDecimal(new BigDecimal(d, RuntimeUtils.getMathContext(env)));
    }


    public static final SDecimal valueOf(final SoloscanExecutor instance,
                                         final String d) {
        return new SDecimal(
                new BigDecimal(d));
    }

    @Override
    public SObjectType getSObjectType() {
        return SObjectType.Decimal;
    }

}
