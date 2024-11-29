package org.soloquest.soloscan;

import lombok.extern.slf4j.Slf4j;
import org.soloquest.soloscan.compiler.lexer.SymbolTable;
import org.soloquest.soloscan.dataset.Row;
import org.soloquest.soloscan.dataset.TerminalRow;
import org.soloquest.soloscan.exception.ExpressionExecuteException;
import org.soloquest.soloscan.runtime.aggfunction.AggFunction;
import org.soloquest.soloscan.utils.Env;
import org.soloquest.soloscan.utils.MiscUtils;
import org.soloquest.soloscan.utils.Preconditions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public abstract class BaseMetricUnitExpression implements MetricUnitExpression {

    private static AtomicInteger INIT_PLACEHOLDER = new AtomicInteger();
    protected final List<AggFunctionUnit> aggFunctionUnits = new ArrayList<>();
    protected final BlockingQueue<Row> queue = new LinkedTransferQueue<>();
    protected final SymbolTable symbolTable;
    protected final SoloscanExecutor instance;
    protected final String expressionString;
    private final String placeHolder;


    public BaseMetricUnitExpression(final SoloscanExecutor instance,
                                    final SymbolTable symbolTable,
                                    String expressionString) {
        this.symbolTable = symbolTable;
        this.instance = instance;
        this.expressionString = expressionString;
        this.placeHolder = ("PH_MU_" + INIT_PLACEHOLDER.getAndIncrement()).toLowerCase();
    }

    public String getPlaceHolder() {
        return placeHolder;
    }

    public void addAggFunctionUnit(AggFunctionUnit aggFunctionUnit) {
        aggFunctionUnits.add(aggFunctionUnit);
    }

    public void processAggFunction(Env env) throws InterruptedException {
        if (this.aggFunctionUnits.size() == 0) {
            log.warn("{} hava no aggregation function", this);
            return;
        }
        boolean hasGrouping = MiscUtils.isMethodOverridden(this.getClass(), MetricUnitExpression.class, "grouping", Env.class);
        Map<String, List<AggFunction>> aggFunctionListMap = new HashMap<>();
        int rowNum = 0;
        int filterNum = 0;
        while (true) {
            Row row = queue.poll(3, TimeUnit.SECONDS);
            Preconditions.checkNotNull(row, "has already consumed " + rowNum + " records!");
            if (row == TerminalRow.INSTANCE) {
                log.info("{} has already processed {} records and filter {} records", this, rowNum, filterNum);
                break;
            }
            rowNum++;
            Env rowEnv = env.newRowEnv(row);
            if (!filter(rowEnv)) {
                filterNum++;
                continue;
            }
            String grouping = grouping(rowEnv);
            List<AggFunction> aggFunctionList = aggFunctionListMap.computeIfAbsent(grouping, k -> aggFunctionUnits.stream().map(aggUnit -> aggUnit.genAggFunction()).collect(Collectors.toList()));
            for (AggFunction aggrFunction : aggFunctionList) {
                aggrFunction.process(rowEnv);
            }
            if (rowNum % 100 == 0) {
                Thread.yield();
            }
        }


        Map<String, Map<String, Object>> aggFunctionMapMap = new HashMap<>();//第一个key是占位符，第二个是groupkey
        for (String groupKey : aggFunctionListMap.keySet()) {
            List<AggFunction> aggFunctionList = aggFunctionListMap.get(groupKey);
            for (AggFunction aggFunction : aggFunctionList) {
                aggFunctionMapMap.compute(aggFunction.getPlaceHolder(), (key, value) -> {
                    if (value == null) {
                        value = new HashMap<>();
                    }
                    value.put(groupKey, aggFunction.getValue());
                    return value;
                });
            }
        }
        if (aggFunctionMapMap.size() == 0) {
            log.warn("{},all data be filter out",expressionString);
            for (AggFunctionUnit aggFunctionUnit : aggFunctionUnits) {
                if (hasGrouping) {
                    env.putAggrValue(aggFunctionUnit.getAggFunctionText().getPlaceHolder(), new HashMap<>());
                } else {
                    env.putAggrValue(aggFunctionUnit.getAggFunctionText().getPlaceHolder(), null);
                }
            }
        } else {
            for (String key : aggFunctionMapMap.keySet()) {
                log.info("{} put the agg value,key:{} and value:{}", this, key, aggFunctionMapMap.get(key));
                if (hasGrouping) {
                    env.putAggrValue(key, aggFunctionMapMap.get(key));
                } else {
                    Map<String, Object> map = aggFunctionMapMap.get(key);
                    Preconditions.checkArgument(map.size() == 1 &&
                                    NO_GROUPING.equalsIgnoreCase(map.keySet().iterator().next()),
                            "the data of no group is invalid");
                    Object v = map.values().iterator().next();
                    env.putAggrValue(key, v);
                    log.info("nogrouping put key:{},value:{}",key,v);
                }
            }
        }


    }

    public boolean consumeRow(Row row) {
        try {
//            queue.put(row);
            Preconditions.checkArgument(queue.offer(row, 2, TimeUnit.SECONDS), "the queue is full");
            return true;
        } catch (InterruptedException e) {
            throw new ExpressionExecuteException("the action of consume row be interrupted");
        }
    }

    public abstract Object execute0(Env env);

    public Object execute(Map<String, Object> map) {
        log.info("{} start to execute,map:{}", this, map);
        Env env;
        if (map instanceof Env) {
            env = (Env) map;
        } else {
            env = new Env(null, this, map);
        }
        try {
            processAggFunction(env);
            Object object = execute0(env);
            env.put(placeHolder, object);
            this.queue.clear();
            return object;
        } catch (InterruptedException e) {
            throw new ExpressionExecuteException(expressionString+" execute fail",e);
        } catch (RuntimeException e) {
            log.error("MetricUnit ["+expressionString+"] execute fail",e);
            if(aggFunctionUnits.size() > 0){
                StringBuilder stringBuilder = new StringBuilder();
                boolean isFirst = true;
                for(AggFunctionUnit aggFunctionUnit:aggFunctionUnits){
                    if(!isFirst){
                        stringBuilder.append(",");
                    }
                    stringBuilder.append(aggFunctionUnit.getAggFunctionText().getPlaceHolder());
                    isFirst = false;
                }
                log.error("AggFunctionUnits's placeholds :{}",stringBuilder.toString());
            }
            throw e;
        }


    }
}
