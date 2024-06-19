package org.soloquest.soloscan.runtime.aggfunction;

import org.soloquest.soloscan.utils.Env;

public class AlwaysTrueAggInner implements AggInner {

    public static final AlwaysTrueAggInner INSTANCE = new AlwaysTrueAggInner();

    @Override
    public boolean check(Env env) {
        return true;
    }
}
