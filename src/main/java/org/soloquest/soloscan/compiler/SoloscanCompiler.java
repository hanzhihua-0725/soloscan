package org.soloquest.soloscan.compiler;

import lombok.extern.slf4j.Slf4j;
import org.soloquest.soloscan.*;
import org.soloquest.soloscan.compiler.codegen.*;
import org.soloquest.soloscan.compiler.lexer.SoloscanLexer;
import org.soloquest.soloscan.compiler.parser.AggFunctionText;
import org.soloquest.soloscan.compiler.parser.AggInnerParser;
import org.soloquest.soloscan.compiler.parser.MetricUnitExpressionParser;
import org.soloquest.soloscan.compiler.parser.SoloExpressionParser;
import org.soloquest.soloscan.exception.ExpressionCompileException;
import org.soloquest.soloscan.exception.ExpressionRuntimeException;
import org.soloquest.soloscan.runtime.aggfunction.AggFunction;
import org.soloquest.soloscan.runtime.aggfunction.AggInner;
import org.soloquest.soloscan.utils.MiscUtils;
import org.soloquest.soloscan.utils.Preconditions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class SoloscanCompiler implements SoloscanCompileInterface {

    private final SoloscanExecutor instance;
    private final SoloscanClassloader classLoader;
    private final Map<String, String> expressionStringMap = new HashMap<>();
    private SoloscanCache<AggFunctionText, AggInner> aggUnitCache = new SoloscanCache<>(true, (text) -> {
        return genAggInner(text);
    });

    private LinkedList<MetricUnitExpression> metricUnitExpressions = new LinkedList<>();
    private LinkedList<AggFunctionUnit> aggFunctionUnits = new LinkedList<>();

    private String filterPart1;
    private String filterPart2;

    public SoloscanCompiler(SoloscanExecutor instance, SoloscanClassloader classLoader, Map<String, String> expressionStringMap, final Map<String, Object> env) {
        this.instance = instance;
        this.classLoader = classLoader;
        this.expressionStringMap.putAll(expressionStringMap);
        if (env != null) {
            if (env.containsKey(SoloscanExecutorExt.FILTER_PART1_KEY)) {
                filterPart1 = env.get(SoloscanExecutorExt.FILTER_PART1_KEY).toString();
            }
            if (env.containsKey(SoloscanExecutorExt.FILTER_PART2_KEY)) {
                filterPart2 = env.get(SoloscanExecutorExt.FILTER_PART2_KEY).toString();
            }
        }
    }

    public Map<String, Expression> compile() {
        return expressionStringMap.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> {
            return compile(e.getValue());
        }));
    }


    public Expression compile(String expressionString) {
        Preconditions.checkNotNullOrEmpty(expressionString, "Blank expression");
        SoloscanLexer lexer = new SoloscanLexer(expressionString);
        SoloExpressionRealCodeGenerator realCodeGenerator = new SoloExpressionRealCodeGenerator(instance, classLoader, BaseSoloExpression.class);
        CodeGeneratorProxy codeGenerator = new CodeGeneratorProxy(realCodeGenerator);
        SoloExpressionParser parser = new SoloExpressionParser(this, instance, lexer, codeGenerator);
        BaseSoloExpression baseSoloExpression = parser.parseSoloExpression();
        Preconditions.checkArgument(aggFunctionUnits.size() == 0, "AggFunctionUnit must be empty");
        while (true) {
            MetricUnitExpression metricUnitExpression = metricUnitExpressions.pollFirst();
            if (metricUnitExpression == null) {
                break;
            }
            baseSoloExpression.addMetricUnit(metricUnitExpression);
        }
        return baseSoloExpression;
    }

    public Expression compileMetricUnit(String expressionString, int index) {
        Preconditions.checkNotNullOrEmpty(expressionString, "Blank expression");

        SoloscanLexer lexer = new SoloscanLexer(expressionString);
        MetricUnitRealCodeGenerator realCodeGenerator = new MetricUnitRealCodeGenerator(instance, classLoader, BaseMetricUnitExpression.class);
        CodeGeneratorProxy codeGenerator = new CodeGeneratorProxy(realCodeGenerator);
        MetricUnitExpressionParser parser = new MetricUnitExpressionParser(this, instance, lexer, codeGenerator);
        if (index == 1 && !MiscUtils.isBlank(filterPart1)) {
            parser.setGlobalFilter(filterPart1);
        } else if (index > 1 && !MiscUtils.isBlank(filterPart2)) {
            parser.setGlobalFilter(filterPart2);
        }
        BaseMetricUnitExpression metricUnitExpression = parser.parseMetricUnitExpression();
        while (true) {
            AggFunctionUnit aggFunctionUnit = aggFunctionUnits.pollFirst();
            if (aggFunctionUnit == null) {
                break;
            }
            metricUnitExpression.addAggFunctionUnit(aggFunctionUnit);
        }
        metricUnitExpressions.add(metricUnitExpression);
        return metricUnitExpression;
    }

    public AggFunctionUnit compileAggFunctionUnit(AggFunctionText aggFunctionText) {
        try {
            AggFunctionUnit aggFunctionUnit;
            Function<AggFunctionText, ? extends AggFunction> function = instance.getAggFunction(aggFunctionText.getName());
            if (function == null) {
                throw new ExpressionCompileException("AggFunction " + aggFunctionText.getName() + " not found");
            }
            if (MiscUtils.isBlank(aggFunctionText.getInnerString())) {
                aggFunctionUnit = AggFunctionUnit.newAggFunctionUnit(aggFunctionText, function, null);
            } else {
                aggFunctionUnit = AggFunctionUnit.newAggFunctionUnit(aggFunctionText, function, aggUnitCache.getR(aggFunctionText));
            }
            aggFunctionUnits.add(aggFunctionUnit);
            return aggFunctionUnit;
        } catch (Exception e) {
            if (e instanceof ExecutionException) {
                if (e.getCause() instanceof ExpressionRuntimeException) {
                    throw (ExpressionRuntimeException) e.getCause();
                }
            }
            throw new ExpressionCompileException(e);
        }
    }

    private AggInner genAggInner(AggFunctionText aggFunctionText) {
        String innerString = aggFunctionText.getInnerString();
        if (!MiscUtils.isBlank(innerString)) {

            SoloscanLexer lexer = new SoloscanLexer(innerString);
            AggInnerRealCodeGenerator realCodeGenerator = new AggInnerRealCodeGenerator(instance, classLoader, AggInner.class);
            CodeGeneratorProxy codeGenerator = new CodeGeneratorProxy(realCodeGenerator);
            AggInnerParser parser = new AggInnerParser(this, instance, lexer, codeGenerator);

            AggInner aggInner = null;
            if (isXAggFunction(aggFunctionText)) {
                aggInner = parser.parseXAggFunctionInner();
            } else {
                aggInner = parser.parseAggFunctionInner();
            }
            return aggInner;
        }
        return null;
    }

    private boolean isXAggFunction(AggFunctionText aggFunctionText) {
        return !aggFunctionText.getName().equals("max") && (aggFunctionText.getName().endsWith("X") || aggFunctionText.getName().endsWith("x"));
    }

}
