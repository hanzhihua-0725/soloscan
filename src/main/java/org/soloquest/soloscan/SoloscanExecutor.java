package org.soloquest.soloscan;

import lombok.extern.slf4j.Slf4j;
import org.soloquest.soloscan.compiler.SoloscanCompiler;
import org.soloquest.soloscan.compiler.codegen.SoloscanClassloader;
import org.soloquest.soloscan.compiler.lexer.SymbolTable;
import org.soloquest.soloscan.compiler.parser.AggFunctionText;
import org.soloquest.soloscan.dataset.DataSet;
import org.soloquest.soloscan.exception.ExpressionExecuteException;
import org.soloquest.soloscan.exception.ExpressionRuntimeException;
import org.soloquest.soloscan.runtime.aggfunction.*;
import org.soloquest.soloscan.runtime.function.*;
import org.soloquest.soloscan.runtime.lang.SFunction;
import org.soloquest.soloscan.utils.MatrixUtils;
import org.soloquest.soloscan.utils.MiscUtils;
import org.soloquest.soloscan.utils.Preconditions;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class SoloscanExecutor {

    public static final SoloscanExecutor INSTANCE = new SoloscanExecutor();
    private SoloscanClassloader classLoader = new SoloscanClassloader(SoloscanExecutor.class.getClassLoader());
    private final Map<String, SFunction> funcMap = new HashMap<>();

    private final ConcurrentHashMap<String, Function<AggFunctionText, ? extends AggFunction>> aggFunctionMap =
            new ConcurrentHashMap<>();

    public void aliasFunction(final String name, final String aliasName) {
        this.addFunction(aliasName, getFunction(name));
    }


    public SoloscanExecutor() {
        loadSystemFunctions();
    }

    public void loadSystemFunctions() {
        this.addFunction(new PercentFunction());
        this.addFunction(new ListFunction());
        this.addFunction(new RangeFunction());
        this.addFunction(new SlideFunction());

        this.aggFunctionMap.put("count", CountAggFunction::new);
        this.aggFunctionMap.put("countblank", CountblankAggFunction::new);
        this.aggFunctionMap.put("average", AvgAggFunction::new);
        this.aggFunctionMap.put("averagex", AvgxAggFunction::new);
        this.aggFunctionMap.put("max", MaxAggFunction::new);
        this.aggFunctionMap.put("maxx", MaxxAggFunction::new);
        this.aggFunctionMap.put("min", MinAggFunction::new);
        this.aggFunctionMap.put("minx", MinxAggFunction::new);
        this.aggFunctionMap.put("sum", SumAggFunction::new);
        this.aggFunctionMap.put("sumx", SumxAggFunction::new);
    }

    public void addAggFunction(String aggFunctionName, Function<AggFunctionText, ? extends AggFunction> function) {
        aggFunctionMap.put(formatAggFunctionName(aggFunctionName), function);
    }

    public Function<AggFunctionText, ? extends AggFunction> getAggFunction(String aggFunctionName) {
        return aggFunctionMap.get(formatAggFunctionName(aggFunctionName));
    }

    public boolean isAggFunction(String aggFunctionName) {
        return aggFunctionMap.containsKey(formatAggFunctionName(aggFunctionName));
    }

    private String formatAggFunctionName(String aggFunctionName) {
        return aggFunctionName.toLowerCase();
    }

    public SoloscanClassloader getSoloscanClassLoader() {
        return this.classLoader;
    }


    public void addFunction(final SFunction function) {
        addFunction(function.getName(), function);
    }

    public void addFunction(final String name, final SFunction function) {
        Preconditions.checkNotNull(function, "Null function");
        Preconditions.checkArgument(!SymbolTable.isReservedKeyword(name), "Invalid function name");
        if (this.funcMap.containsKey(name)) {
            log.warn("The function '{}' is already exists, but is replaced with new one. ", name);
        }
        this.funcMap.put(name, function);
    }

    public SFunction removeFunction(final String name) {
        return (SFunction) this.funcMap.remove(name);
    }

    public SFunction getFunction(final String name) {
        SFunction function = this.funcMap.get(name);
        if (function == null) {
            log.warn("function '{}' does not exist!", name);
        }
        return function;
    }

    public Object execute(final String expression) {
        return execute(expression, null, DataSet.EMPTY);
    }

    public Object execute(final String expression, final Map<String, Object> env) {
        return execute(expression, env, null);
    }

    public Object execute(final String expression, final DataSet dataSet) {
        return execute(expression, null, dataSet);
    }

    Object execute(final String expression, final Map<String, Object> env,
                   final DataSet dataSet) {
        try {
            long start = System.currentTimeMillis();
            Map<String, String> expressionStringMap = new HashMap();
            expressionStringMap.put("row1", expression);
            Map<String, Expression> compiledExpressionMap = getExpression(expressionStringMap,env);
            Preconditions.checkArgument(dataSet != DataSet.EMPTY, "empty dataset");
            if (dataSet != DataSet.EMPTY)
                DataProvider.work(dataSet, compiledExpressionMap.values().toArray(new Expression[0]));
            Object object = compiledExpressionMap.values().iterator().next().execute(env);
            log.info("executeList cost :{} ms", (System.currentTimeMillis() - start));
            return object;
        } catch (ExpressionRuntimeException ere) {
            throw ere;
        } catch (Exception e) {
            throw new ExpressionExecuteException(e);
        }
    }

    public Map<String, Object> execute(final Map<String, String> expressionMap, final DataSet dataSet) {
        return execute(expressionMap, null, dataSet);
    }

    public Map<String, Object> execute(final Map<String, String> expressionStringMap, final Map<String, Object> env, final DataSet dataSet) {
        int executeTimeoutMs = SoloscanOptions.getOption(SoloscanOptions.EXECUTE_TIMEOUT_MS);
        long endTime = -1;
        long timeLeft = -1;
        ExecutorService singleThreadExecutor = null;
        ForkJoinPool forkJoinPool = null;
        if(executeTimeoutMs > 0){
            endTime = System.currentTimeMillis() + executeTimeoutMs;
            timeLeft = executeTimeoutMs;
        }
        try {
            Map<String, Expression> compiledExpressionMap = getExpression(expressionStringMap,env);
            Preconditions.checkArgument(expressionStringMap.size() == compiledExpressionMap.size(), "");
            if (dataSet != DataSet.EMPTY)
                DataProvider.work(dataSet, compiledExpressionMap.values().toArray(new Expression[0]));
            log.info("expressionStringMap:{},compile expression:{},size:{}", expressionStringMap, compiledExpressionMap, compiledExpressionMap.size());
            if(executeTimeoutMs > 0){
                timeLeft = endTime - System.currentTimeMillis();
                if (timeLeft <= 0) {
                    throw new TimeoutException("Execute timeout on compile time");
                }
            }
            Map<String, Object> resultMap;
            if (compiledExpressionMap.size() == 1) {
                resultMap = new HashMap<>();
                Object object = null;
                if(timeLeft > 0){
                    singleThreadExecutor = Executors.newSingleThreadExecutor();
                    Future<Object> future =singleThreadExecutor.submit(()->compiledExpressionMap.values().iterator().next().execute(env));
                    object = future.get(timeLeft, TimeUnit.MILLISECONDS);
                }else{
                    object = compiledExpressionMap.values().iterator().next().execute(env);
                }
                resultMap.put(compiledExpressionMap.keySet().iterator().next(), object);
            } else {
//                resultMap = new ConcurrentHashMap<>(compiledExpressionMap.size());
                resultMap = Collections.synchronizedMap(new HashMap<>(compiledExpressionMap.size()));
                forkJoinPool = new ForkJoinPool(compiledExpressionMap.size());
                List<Future<Object>> futureList = forkJoinPool.invokeAll(compiledExpressionMap.entrySet().stream().map(entry -> {
                            return new Callable<Object>() {
                                public Object call() throws Exception {
                                    resultMap.put(entry.getKey(), entry.getValue().execute(env));
                                    return null;
                                }
                            };
                        }
                ).collect(Collectors.toList()));

                if(executeTimeoutMs == 0){
                    for(Future future:futureList){
                        future.get();
                    }
                }else{
                    int index = 0;
                    for(Future future:futureList){
                        timeLeft = endTime - System.currentTimeMillis();
                        if (timeLeft <= 0) {
                            throw new TimeoutException("Execute timeout,size:"+futureList.size()+", and timeout on index:"+index);
                        }
                        future.get(timeLeft, TimeUnit.MILLISECONDS);
                        index ++;
                    }
                }
            }
            return resultMap;
        } catch (ExpressionRuntimeException ere) {
            throw ere;
        } catch (Exception e) {
            if (e instanceof ExecutionException) {
                if (e.getCause() instanceof ExpressionRuntimeException) {
                    throw (ExpressionRuntimeException) e.getCause();
                }
            }
            throw new ExpressionExecuteException(e);
        }finally {
            if(singleThreadExecutor != null){
                singleThreadExecutor.shutdown();
            }
            if (forkJoinPool != null){
                forkJoinPool.shutdown();
            }
        }
    }

    private Map<String, Expression> getExpression(final Map<String, String> expressionStringMap,final Map<String, Object> env) {
        long start = System.currentTimeMillis();
        SoloscanCompiler soloscanCompiler = new SoloscanCompiler(SoloscanExecutor.this, this.classLoader, expressionStringMap,env);
        Map<String, Expression> compiledExpressionMap = soloscanCompiler.compile();
        Preconditions.checkNotNull(compiledExpressionMap);
        log.info("compile expression, expression:{},cost: {}.ms", compiledExpressionMap, (System.currentTimeMillis() - start));
        return compiledExpressionMap;
    }





}
