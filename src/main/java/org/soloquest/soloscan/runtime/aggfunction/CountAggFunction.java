package org.soloquest.soloscan.runtime.aggfunction;

import org.soloquest.soloscan.compiler.parser.AggFunctionText;
import org.soloquest.soloscan.utils.Env;

public class CountAggFunction extends AbstractAggFunction {

    private long initCount;
    private long count = initCount;

    public CountAggFunction(AggFunctionText text) {
        super(text);
    }

    @Override
    public Number getValue0() {
        return count;
    }


    @Override
    protected void doProcess(Env env) {
        count++;
    }
}
