package org.soloquest.soloscan.runtime.aggfunction;

import org.soloquest.soloscan.compiler.parser.AggFunctionText;
import org.soloquest.soloscan.exception.ExpressionExecuteException;
import org.soloquest.soloscan.utils.Env;

public class AvgAggFunction extends AbstractAggFunction {
    protected double sum;
    protected long count;


    public AvgAggFunction(AggFunctionText text) {
        super(text);
    }

    @Override
    protected void doProcess(Env env) {
        count++;
        Object object = aggInner.getInnerValue(env);
        if (object instanceof Number) {
            sum += ((Number) object).doubleValue();
        } else {
            throw new ExpressionExecuteException(object + " is not a number");
        }


    }

    @Override
    public Double getValue0() {
        if (count == 0) {
            return 0.0;
        }
        return sum / count;
    }

}
