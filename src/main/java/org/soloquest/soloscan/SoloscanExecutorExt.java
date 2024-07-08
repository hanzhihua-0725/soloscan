package org.soloquest.soloscan;

import org.soloquest.soloscan.dataset.DataSet;
import org.soloquest.soloscan.utils.MatrixUtils;
import org.soloquest.soloscan.utils.MiscUtils;
import org.soloquest.soloscan.utils.Preconditions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class SoloscanExecutorExt extends SoloscanExecutor {

    public static final SoloscanExecutorExt INSTANCE = new SoloscanExecutorExt();
    //    public static final String META_KEY = ".meta";
    public static final String FILTER_PART1_KEY = ".part1key";
    public static final String FILTER_PART2_KEY = ".part2key";

    public Object executeList(List<String> expressionList, DataSet dataSet) {
        if (expressionList == null || expressionList.size() == 0) {
            return null;
        }
        final AtomicInteger index = new AtomicInteger();
        Map<String, String> expressionStringMap = expressionList.stream().collect(Collectors.toMap(e -> {
            return "item" + index.incrementAndGet();
        }, e -> e));
        return execute(expressionStringMap, dataSet);
    }

    public Map<String, Object> executeWithPlaceHold(final Map<String, String> expressionMap, final Map<String, String> replaceHoldMap, final DataSet dataSet) {
        MiscUtils.applyPlaceHolder(expressionMap, replaceHoldMap);
        return execute(expressionMap, null, dataSet);
    }


    public Map<String, Object> executeMatrix(final Map<String, String> expressionMap, int rows, int columns, final DataSet dataSet) {
        Preconditions.checkNotNull(expressionMap);
        Preconditions.checkArgument(MatrixUtils.isComplete2DArray(expressionMap, rows, columns), expressionMap + " is not a complete 2d array!");
        Map<String, Number> result = MiscUtils.forciblyCast(execute(expressionMap, null, dataSet));
        return MiscUtils.forciblyCast(MatrixUtils.clcalculateMatrix(result, rows, columns));
    }

    public Map<String, Object> executeWithGlobalFilter(final Map<String, String> expressionMap, String filterPart1, String filterPart2, final DataSet dataSet) {
        Map<String, Object> env = new HashMap<>();
        env.put(FILTER_PART1_KEY, filterPart1);
        env.put(FILTER_PART2_KEY, filterPart2);
        return execute(expressionMap, env, dataSet);
    }

    public Map<String, Object> executeWithPlaceHoldAndGlobalFilter(final Map<String, String> expressionMap, final Map<String, String> replaceHoldMap,String filterPart1, String filterPart2, final DataSet dataSet) {
        MiscUtils.applyPlaceHolder(expressionMap, replaceHoldMap);
        Map<String, Object> env = new HashMap<>();
        env.put(FILTER_PART1_KEY, filterPart1);
        env.put(FILTER_PART2_KEY, filterPart2);
        return execute(expressionMap, env, dataSet);
    }
}
