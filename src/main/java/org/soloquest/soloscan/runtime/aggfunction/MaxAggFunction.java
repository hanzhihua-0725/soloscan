package org.soloquest.soloscan.runtime.aggfunction;

import org.soloquest.soloscan.compiler.parser.AggFunctionText;
import org.soloquest.soloscan.exception.ExpressionExecuteException;
import org.soloquest.soloscan.utils.Env;

public class MaxAggFunction extends AbstractAggFunction {

    private long initCount;
    private long value = Integer.MIN_VALUE;

    public MaxAggFunction(AggFunctionText text) {
        super(text);
    }


    @Override
    public Number getValue() {
        if (value == Integer.MIN_VALUE) {
            this.value = 0;
        }
        return value;
    }


    @Override
    protected void doProcess(Env env) {
        Object object = aggInner.getInnerValue(env);
        if (object instanceof Number) {
            long i = ((Number) object).longValue();
            if (i > this.value) {
                this.value = i;
            }
        } else {
            throw new ExpressionExecuteException(object + " is not a number");
        }
    }

}
