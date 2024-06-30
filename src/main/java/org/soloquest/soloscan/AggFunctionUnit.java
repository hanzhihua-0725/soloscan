package org.soloquest.soloscan;

import lombok.Data;
import org.soloquest.soloscan.compiler.parser.AggFunctionText;
import org.soloquest.soloscan.runtime.aggfunction.AggFunction;
import org.soloquest.soloscan.runtime.aggfunction.AggInner;
import org.soloquest.soloscan.runtime.aggfunction.AlwaysTrueAggInner;

import java.util.function.Function;

@Data
public class AggFunctionUnit {

    private Function<AggFunctionText, ? extends AggFunction> aggFunctionGenerator;
    private AggFunctionText aggFunctionText;
    private AggInner aggInner = AlwaysTrueAggInner.INSTANCE;

    public static AggFunctionUnit newAggFunctionUnit(AggFunctionText aggFunctionText, Function<AggFunctionText, ? extends AggFunction> function, AggInner aggInner) {
        AggFunctionUnit aggFunctionUnit = new AggFunctionUnit();
        aggFunctionUnit.setAggFunctionText(aggFunctionText);
        aggFunctionUnit.setAggFunctionGenerator(function);
        if (aggInner != null) {
            aggFunctionUnit.setAggInner(aggInner);
        }
        return aggFunctionUnit;
    }

    public AggFunction genAggFunction() {
        AggFunction aggFunction = aggFunctionGenerator.apply(aggFunctionText);
        aggFunction.setFilter(aggInner);
        return aggFunction;
    }

}
