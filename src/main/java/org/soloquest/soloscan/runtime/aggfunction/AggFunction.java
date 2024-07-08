package org.soloquest.soloscan.runtime.aggfunction;

import org.soloquest.soloscan.utils.Env;

public interface AggFunction {
    Number getValue();

    AggInner getFilter();

    void setFilter(AggInner aggInner);

    void process(Env env);

    String getPlaceHolder();

    default String getTopNFilter() {
        return "";
    }


}
