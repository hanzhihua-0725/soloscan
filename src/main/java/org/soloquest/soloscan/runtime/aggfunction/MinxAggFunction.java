package org.soloquest.soloscan.runtime.aggfunction;

import org.soloquest.soloscan.compiler.parser.AggFunctionText;

public class MinxAggFunction extends MinAggFunction implements TwoArgAggFunction {

    public MinxAggFunction(AggFunctionText text) {
        super(text);
    }

}
