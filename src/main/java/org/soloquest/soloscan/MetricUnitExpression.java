package org.soloquest.soloscan;

import org.soloquest.soloscan.dataset.Row;
import org.soloquest.soloscan.utils.Env;

public interface MetricUnitExpression extends Expression{

    final static String NO_GROUPING = "__NO_GROUPING";

    default String grouping(Env rowEnv) {
        return NO_GROUPING;
    }

    default boolean filter(Env rowEnv) {
        return true;
    }

    String getPlaceHolder();
}
