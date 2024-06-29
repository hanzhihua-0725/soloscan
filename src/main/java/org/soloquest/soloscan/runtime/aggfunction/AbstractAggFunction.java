package org.soloquest.soloscan.runtime.aggfunction;

import org.soloquest.soloscan.compiler.parser.AggFunctionText;
import org.soloquest.soloscan.utils.Env;

public abstract class AbstractAggFunction implements AggFunction {

    protected AggInner aggInner = AlwaysTrueAggInner.INSTANCE;

    protected AggFunctionText text;

    public AbstractAggFunction(AggFunctionText text) {
        this.text = text;
    }


    public void setFilter(AggInner aggInner) {
        this.aggInner = aggInner;
    }

    @Override
    public AggInner getFilter() {
        return this.aggInner;
    }

    @Override
    public void process(Env env) {
        if (aggInner.check(env)) {
            doProcess(env);
        }
    }

    @Override
    public String getPlaceHolder() {
        return this.text.getPlaceHolder();
    }

    protected abstract void doProcess(Env env);
}
