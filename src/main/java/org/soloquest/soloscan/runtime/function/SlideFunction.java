package org.soloquest.soloscan.runtime.function;

import org.soloquest.soloscan.runtime.lang.*;
import org.soloquest.soloscan.utils.MetricUtils;

import java.util.Map;

public class SlideFunction extends AbstractFunction {

    @Override
    public String getName() {
        return "slide";
    }

    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2) {
        if (arg1 instanceof SMertric && arg2 instanceof SLong) {
            SMertric smetric = (SMertric) arg1;
            int windowSize = (int) ((SLong) arg2).longValue();
            return new SRuntimeMertic(MetricUtils.slideAgg((Map<String, Number>) smetric.getValue(env), windowSize));
        }
        return throwArity(2);
    }

    public SObject call(final Map<String, Object> env, final SObject arg1,final SObject arg2,final SObject arg3){
        if (arg1 instanceof SMertric && arg2 instanceof SLong && arg3 instanceof SLong) {
            SMertric smetric = (SMertric) arg1;
            int windowSize = (int) ((SLong) arg2).longValue();
            int stepSize = (int) ((SLong) arg3).longValue();
            return new SRuntimeMertic(MetricUtils.slideAgg((Map<String, Number>) smetric.getValue(env), windowSize,stepSize));
        }
        return throwArity(2);
    }
}
