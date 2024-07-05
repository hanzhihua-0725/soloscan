package org.soloquest.soloscan.compiler;

import org.soloquest.soloscan.*;
import org.soloquest.soloscan.compiler.codegen.MockCodeGenerator;
import org.soloquest.soloscan.compiler.lexer.SoloscanLexer;
import org.soloquest.soloscan.compiler.parser.AggFunctionText;
import org.soloquest.soloscan.compiler.parser.AggInnerParser;
import org.soloquest.soloscan.compiler.parser.MetricUnitExpressionParser;
import org.soloquest.soloscan.compiler.parser.SoloExpressionParser;
import org.soloquest.soloscan.exception.ExpressionCompileException;
import org.soloquest.soloscan.runtime.aggfunction.AggFunction;
import org.soloquest.soloscan.utils.Env;
import org.soloquest.soloscan.utils.MiscUtils;
import org.soloquest.soloscan.utils.Preconditions;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class SololscanValidator implements SoloscanCompileInterface {

    final static Expression SOLOEXPRESSION = new BaseSoloExpression(SoloscanExecutorExt.INSTANCE, null, "in validator") {
        @Override
        public Object execute0(Env env) {
            return null;
        }
    };
    final static Expression METRICUNITEXPRESSION = new BaseMetricUnitExpression(SoloscanExecutorExt.INSTANCE, null, "in validator") {
        @Override
        public Object execute0(Env env) {
            return null;
        }
    };
    final MockCodeGenerator codeGenerator = new MockCodeGenerator();

    public ValidatorResult validate(String expressionString, List<String> columnList) {
        Preconditions.checkNotNullOrEmpty(expressionString, "Blank expression");
        ValidatorResult vr = new ValidatorResult();
        compile(expressionString);
        List vars = codeGenerator.getVariables();
        if (vars.size() == 0)
            return vr;
        if (columnList.size() == 0) {
            vr.setErrorMessage(vars + " was not defined");
            return vr;
        }
        Set<String> missSet = null;
        if (SoloscanOptions.getOption(SoloscanOptions.COLUMN_CASE_INSENSITIVE)) {
            missSet = MiscUtils.findMissingElementsCaseInsensitive(columnList, vars);
        } else {
            missSet = MiscUtils.findMissingElementsCaseSensitive(columnList, vars);
        }
        if (missSet == null || missSet.size() == 0) {
            return vr;
        }
        vr.setErrorMessage(missSet + " was not defined");
        return vr;
    }

    @Override
    public Expression compile(String expressionString) {
        SoloscanLexer lexer = new SoloscanLexer(expressionString);
        SoloExpressionParser parser = new SoloExpressionParser(this, SoloscanExecutorExt.INSTANCE, lexer, codeGenerator);
        codeGenerator.setParser(parser);
        parser.parseSoloExpression();
        return SololscanValidator.SOLOEXPRESSION;
    }

    @Override
    public Expression compileMetricUnit(String expressionString, int index) {
        SoloscanLexer lexer = new SoloscanLexer(expressionString);
        MetricUnitExpressionParser parser = new MetricUnitExpressionParser(this, SoloscanExecutorExt.INSTANCE, lexer, codeGenerator);
        codeGenerator.setParser(parser);
        parser.parseMetricUnitExpression();
        return SololscanValidator.METRICUNITEXPRESSION;
    }

    @Override
    public AggFunctionUnit compileAggFunctionUnit(AggFunctionText aggFunctionText) {
        AggFunctionUnit aggFunctionUnit;
        Function<AggFunctionText, ? extends AggFunction> function = SoloscanExecutorExt.INSTANCE.getAggFunction(aggFunctionText.getName());
        if (function == null) {
            throw new ExpressionCompileException("AggFunction " + aggFunctionText.getName() + " not found");
        }
        if (!MiscUtils.isBlank(aggFunctionText.getInnerString())) {
            String innerString = aggFunctionText.getInnerString();
            SoloscanLexer lexer = new SoloscanLexer(innerString);

            AggInnerParser parser = new AggInnerParser(this, SoloscanExecutorExt.INSTANCE, lexer, codeGenerator);
            codeGenerator.setParser(parser);
            if (isXAggFunction(aggFunctionText)) {
                parser.parseXAggFunctionInner();
            } else {
                parser.parseAggFunctionInner();
            }

        }
        return null;
    }

    private boolean isXAggFunction(AggFunctionText aggFunctionText) {
        return !aggFunctionText.getName().equals("max") && (aggFunctionText.getName().endsWith("X") || aggFunctionText.getName().endsWith("x"));
    }
}
