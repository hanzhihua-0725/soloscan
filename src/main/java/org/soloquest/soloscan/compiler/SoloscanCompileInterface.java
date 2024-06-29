package org.soloquest.soloscan.compiler;

import org.soloquest.soloscan.AggFunctionUnit;
import org.soloquest.soloscan.Expression;
import org.soloquest.soloscan.compiler.parser.AggFunctionText;

public interface SoloscanCompileInterface {

    Expression compile(String expressionString);

    Expression compileMetricUnit(String expressionString, int index);

    AggFunctionUnit compileAggFunctionUnit(AggFunctionText aggFunctionText);
}
