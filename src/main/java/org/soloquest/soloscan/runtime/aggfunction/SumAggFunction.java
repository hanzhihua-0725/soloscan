package org.soloquest.soloscan.runtime.aggfunction;

import org.soloquest.soloscan.compiler.parser.AggFunctionText;
import org.soloquest.soloscan.exception.ExpressionExecuteException;
import org.soloquest.soloscan.utils.Env;

public class SumAggFunction extends AbstractAggFunction {

    private long initCount;
    private long value = 0;

    public SumAggFunction(AggFunctionText text) {
        super(text);
    }

    @Override
    public Number getValue() {
        return value;
    }


    @Override
    protected void doProcess(Env env) {
        Object object = aggInner.getInnerValue(env);
        if (object instanceof Number) {
            long i = ((Number) object).intValue();
            this.value += i;
        } else {
            throw new ExpressionExecuteException(object + " is not a number");

        }
    }

}
