package org.soloquest.soloscan.runtime.function;

import org.soloquest.soloscan.SoloscanExecutorExt;
import org.soloquest.soloscan.compiler.codegen.AggInnerRealCodeGenerator;
import org.soloquest.soloscan.compiler.codegen.CodeGeneratorProxy;
import org.soloquest.soloscan.compiler.lexer.SoloscanLexer;
import org.soloquest.soloscan.compiler.parser.AggInnerParser;
import org.soloquest.soloscan.dataset.DataSet;
import org.soloquest.soloscan.dataset.Row;
import org.soloquest.soloscan.exception.ExpressionExecuteException;
import org.soloquest.soloscan.runtime.aggfunction.AggInner;
import org.soloquest.soloscan.runtime.lang.AbstractFunction;
import org.soloquest.soloscan.runtime.lang.SLong;
import org.soloquest.soloscan.runtime.lang.SObject;
import org.soloquest.soloscan.runtime.lang.SString;
import org.soloquest.soloscan.utils.Env;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class TopNFunction extends AbstractFunction {
    @Override
    public String getName() {
        return "topn";
    }

    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2) {
        DataSet dataSet = (DataSet) env.get(Env.DATASET_KEY);
        if (dataSet == null) {
            throw new ExpressionExecuteException("dataset is null in a topn function");
        }
        dataSet = dataSet.newDataSet();
        if (arg1 instanceof SString && arg2 instanceof SLong) {
            String column = ((SString) arg1).stringValue(env);
            int n = (int) ((SLong) arg2).longValue();
            TopN topN = new TopN(column, n);
            topN.addDataSet(dataSet);
            return new SString(topN.getTopN());
        }
        return throwArity(2);
    }

    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3) {
        DataSet dataSet = (DataSet) env.get(Env.DATASET_KEY);
        if (dataSet == null) {
            throw new ExpressionExecuteException("dataset is null in a topn function");
        }
        dataSet = dataSet.newDataSet();
        if (arg1 instanceof SString && arg2 instanceof SLong && arg3 instanceof SString) {
            String column = ((SString) arg1).stringValue(env);
            int n = (int) ((SLong) arg2).longValue();
            String filterString = ((SString) arg3).stringValue(env);
            AggInner aggInner = genAggInner(filterString);
            TopN topN = new TopN(column, n, aggInner);
            topN.addDataSet(dataSet);
            return new SString(topN.getTopN());
        }
        return throwArity(2);
    }

    public AggInner genAggInner(String filterString) {
        SoloscanLexer lexer = new SoloscanLexer(filterString);
        AggInnerRealCodeGenerator realCodeGenerator = new AggInnerRealCodeGenerator(SoloscanExecutorExt.INSTANCE, SoloscanExecutorExt.INSTANCE.getSoloscanClassLoader(), AggInner.class);
        CodeGeneratorProxy codeGenerator = new CodeGeneratorProxy(realCodeGenerator);
        AggInnerParser parser = new AggInnerParser(null, SoloscanExecutorExt.INSTANCE, lexer, codeGenerator);
        return parser.parseAggFunctionInner();
    }

    class TopN {
        private TreeSet<Object> sortedSet;
        private int n;
        private String key;

        private AggInner aggInner;

        public TopN(String key, int n) {
            this(key, n, null);
        }

        public TopN(String key, int n, AggInner aggInner) {
            this.n = n;
            this.key = key;
            this.aggInner = aggInner;
            this.sortedSet = new TreeSet<>(new Comparator<Object>() {
                @Override
                public int compare(Object value1, Object value2) {

                    int valueComparison;
                    if (value1 instanceof Number && value2 instanceof Number) {
                        valueComparison = Double.compare(((Number) value2).doubleValue(), ((Number) value1).doubleValue());
                    } else if (value1 instanceof String && value2 instanceof String) {
                        valueComparison = ((String) value2).compareTo((String) value1);
                    } else {
                        throw new IllegalArgumentException("Unsupported data types for comparison");
                    }
                    return valueComparison;
                }
            });
        }

        public void addDataSet(DataSet dataSet) {
            Env parentEnv = new Env(null, null, new HashMap<>());
            while (dataSet.hasNext()) {
                Row row = dataSet.next();
                Env env = parentEnv.newRowEnv(row);
                if (aggInner != null && !aggInner.check(env)) {
                    continue;
                }
                Object value = row.getValue(key);
                sortedSet.add(value);
            }
            while (sortedSet.size() > n) {
                sortedSet.pollLast();
            }
        }

        public String getTopN() {
            return sortedSet.stream()
                    .limit(n)
                    .collect(Collectors.toList()).toString();
        }
    }
}
