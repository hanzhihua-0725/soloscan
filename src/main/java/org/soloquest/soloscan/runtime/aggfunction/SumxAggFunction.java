package org.soloquest.soloscan.runtime.aggfunction;

import org.soloquest.soloscan.compiler.parser.AggFunctionText;

public class SumxAggFunction extends SumAggFunction implements TwoArgAggFunction {
    public SumxAggFunction(AggFunctionText text) {
        super(text);
    }

}
