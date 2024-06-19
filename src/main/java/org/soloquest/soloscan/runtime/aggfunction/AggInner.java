package org.soloquest.soloscan.runtime.aggfunction;

import org.soloquest.soloscan.utils.Env;

public interface AggInner {

    boolean check(Env env);

    default Object getInnerValue(Env env) {
        return null;
    }
}
