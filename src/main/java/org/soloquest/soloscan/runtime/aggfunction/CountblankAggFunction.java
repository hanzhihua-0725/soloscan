package org.soloquest.soloscan.runtime.aggfunction;

import org.soloquest.soloscan.compiler.parser.AggFunctionText;
import org.soloquest.soloscan.utils.Env;

public class CountblankAggFunction extends AbstractAggFunction {
    private long initCount;
    private long count = initCount;

    public CountblankAggFunction(AggFunctionText text) {
        super(text);
        setFilter(env -> {
            return !env.containsKey(text.getInnerString());
        });
    }

    @Override
    protected void doProcess(Env env) {
        count++;
    }

    @Override
    public Number getValue() {
        return count;
    }

}
