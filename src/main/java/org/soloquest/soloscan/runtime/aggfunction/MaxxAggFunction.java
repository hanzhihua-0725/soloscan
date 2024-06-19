package org.soloquest.soloscan.runtime.aggfunction;

import org.soloquest.soloscan.compiler.parser.AggFunctionText;

public class MaxxAggFunction extends MaxAggFunction implements TwoArgAggFunction {

    public MaxxAggFunction(AggFunctionText text) {
        super(text);
    }

}
