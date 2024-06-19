package org.soloquest.soloscan.runtime.function;

import org.soloquest.soloscan.runtime.lang.AbstractFunction;
import org.soloquest.soloscan.runtime.lang.SObject;
import org.soloquest.soloscan.runtime.lang.SRuntimeJavaType;

import java.util.Map;

public class RangeFunction extends AbstractFunction {
    @Override
    public String getName() {
        return "range";
    }

    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2) {
        IntRange range = new IntRange(covert(arg1.getValue(env)), covert(arg2.getValue(env)));
        return SRuntimeJavaType.valueOf(range);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3) {
        IntRange range = new IntRange(covert(arg1.getValue(env)), covert(arg2.getValue(env)), covert(arg3.getValue(env)));
        return SRuntimeJavaType.valueOf(range);
    }

    private int covert(Object object) {
        if (object instanceof Number) {
            return ((Number) object).intValue();
        }
        throw new UnsupportedOperationException(object + "is not a number");
    }
}
