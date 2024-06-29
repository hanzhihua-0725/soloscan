package org.soloquest.soloscan;

import lombok.extern.slf4j.Slf4j;
import org.soloquest.soloscan.compiler.lexer.SymbolTable;
import org.soloquest.soloscan.dataset.Row;
import org.soloquest.soloscan.utils.Env;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public abstract class BaseSoloExpression implements Expression {
    protected final SymbolTable symbolTable;

    protected final SoloscanExecutor instance;

    protected final String expressionString;
    protected final List<MetricUnitExpression> metricUnitExpressions = new CopyOnWriteArrayList<>();

    public void addMetricUnit(MetricUnitExpression metricUnitExpression) {
        metricUnitExpressions.add(metricUnitExpression);
    }


    public BaseSoloExpression(final SoloscanExecutor instance,
                              final SymbolTable symbolTable,
                              String expressionString) {
        this.symbolTable = symbolTable;
        this.instance = instance;
        this.expressionString = expressionString;
    }

    public abstract Object execute0(Env env);

    @Override
    public String toString() {
        return "BaseSoloExpression{" +
                "expressionString='" + expressionString + '\'' +
                '}';
    }

    public Object execute(Map<String, Object> map) {
        log.info("{} start to execute,map:{}", this, map);
        if (map == null) {
            map = Collections.synchronizedMap(new HashMap<>());
        } else {
            map = Collections.synchronizedMap(map);
        }
        Env env = new Env(this.instance, this, map);
        metricUnitExpressions.stream().forEach(metricUnitExpression -> {
            metricUnitExpression.execute(env);
        });
        return execute0(env);
    }

    public boolean consumeRow(Row row) {
        for (MetricUnitExpression metricUnitExpression : metricUnitExpressions) {
            metricUnitExpression.consumeRow(row);
        }
        return true;
    }

}

