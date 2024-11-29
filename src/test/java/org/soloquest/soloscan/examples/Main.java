package org.soloquest.soloscan.examples;

import org.soloquest.soloscan.SoloscanExecutorExt;
import org.soloquest.soloscan.SoloscanOptions;
import org.soloquest.soloscan.compiler.parser.AggFunctionText;
import org.soloquest.soloscan.dataset.DataSet;
import org.soloquest.soloscan.dataset.ListDataSet;
import org.soloquest.soloscan.exception.ExpressionCompileException;
import org.soloquest.soloscan.runtime.aggfunction.AbstractAggFunction;
import org.soloquest.soloscan.runtime.lang.AbstractFunction;
import org.soloquest.soloscan.runtime.lang.SObject;
import org.soloquest.soloscan.runtime.lang.SString;
import org.soloquest.soloscan.utils.Env;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {


        Map map = new HashMap<>();
        map.put("a", false);
        map.put("b", 1.23);
        List<Map> list = new ArrayList<>();
        map.put("data", list);
        Map p = new HashMap();
        p.put("col1", 10);
        p.put("groupkey", "a");
        list.add(p);
        p = new HashMap();
        p.put("col1", 2);
        p.put("groupkey", "b");
        list.add(p);
        p = new HashMap();
        p.put("col1", 3);
        p.put("groupkey", "b");
        list.add(p);
        DataSet dataSet = new ListDataSet<>(list);
        SoloscanOptions.set(SoloscanOptions.GENERATE_CLASS.key(), true);
        SoloscanExecutorExt instance = SoloscanExecutorExt.INSTANCE;
        instance.addAggFunction("ds", DoubleSum::new);
        List<String> expressions = new ArrayList<>();
//        expressions.add("count(col1)+count();groupkey");
//        expressions.add("count(col1)-count();groupkey");
//        expressions.add("count(col1)*count();groupkey");
//        expressions.add("count(col1)/count();groupkey");
//        System.out.println(instance.executeList(expressions,dataSet));
//        dataSet = new ListDataSet<>(list);
//        expressions = new ArrayList<>();
//        expressions.add("count(col1)+count();groupkey");
//        expressions.add("count(col1)-count();groupkey");
//        expressions.add("count(col1)*count();groupkey");
//        expressions.add("count(col1)/count();groupkey");
        expressions.add("{count()}");
        System.out.println(instance.executeList(expressions, dataSet));


    }

    static class AFunction extends AbstractFunction {

        @Override
        public String getName() {
            return "afunc";
        }

        public SObject call(final Map<String, Object> env) {

            return new SString("afunc：");
        }

    }

    static class BFunction extends AbstractFunction {

        @Override
        public String getName() {
            return "bfunc";
        }

        public SObject call(final Map<String, Object> env) {

            return new SString("bfunc：");
        }
    }

    static class DoubleSum extends AbstractAggFunction {
        private long initCount;
        private long value = 0;

        public DoubleSum(AggFunctionText text) {
            super(text);
        }

        @Override
        public Number getValue0() {
            return value;
        }

        @Override
        protected void doProcess(Env env) {
            Object object = aggInner.getInnerValue(env);
            if (object instanceof Number) {
                long i = ((Number) object).longValue() * 2;
                this.value += i;
            } else {
                throw new ExpressionCompileException("[" + object.getClass().getSimpleName() + "]" + object + " is not a number");

            }
        }
    }


}