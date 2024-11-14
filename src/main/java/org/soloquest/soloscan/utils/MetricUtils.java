package org.soloquest.soloscan.utils;

import lombok.extern.slf4j.Slf4j;
import org.soloquest.soloscan.exception.ExpressionExecuteException;
import org.soloquest.soloscan.runtime.lang.Numbers;

import java.util.*;

@Slf4j
public class MetricUtils {

    public static Object add(Object metrics1, Object metrics2) throws IllegalArgumentException {
        checkMetrics(metrics1, metrics2);
        Object result = null;
        if (metrics1 instanceof HashMap) {
            Map<String, Number> map1 = (HashMap<String, Number>) metrics1;
            Map<String, Number> map2 = (HashMap<String, Number>) metrics2;
            result = new HashMap<String, Number>();
            for (String key : map1.keySet()) {
                Number value1 = map1.get(key);
                Number value2 = map2.get(key);
                ((HashMap) result).put(key, Numbers.add(value1, value2));
            }

        } else if (metrics1 instanceof Number) {
            Number value1 = (Number) metrics1;
            Number value2 = (Number) metrics2;
            result = Numbers.add(value1, value2);
        }
        logMetric(metrics1, metrics2, result, "+");
        return result;
    }

    public static Object sub(Object metrics1, Object metrics2) throws IllegalArgumentException {
        checkMetrics(metrics1, metrics2);
        Object result = null;
        if (metrics1 instanceof HashMap) {
            Map<String, Number> map1 = (HashMap<String, Number>) metrics1;
            Map<String, Number> map2 = (HashMap<String, Number>) metrics1;
            result = new HashMap<String, Number>();
            for (String key : map1.keySet()) {
                Number value1 = map1.get(key);
                Number value2 = map2.get(key);
                ((HashMap) result).put(key, Numbers.minus(value1, value2));
            }

        } else if (metrics1 instanceof Number) {
            Number value1 = (Number) metrics1;
            Number value2 = (Number) metrics2;
            result = Numbers.minus(value1, value2);
        }
        logMetric(metrics1, metrics2, result, "-");
        return result;

    }

    public static Object mult(Object metrics1, Object metrics2) throws IllegalArgumentException {
        checkMetrics(metrics1, metrics2);
        Object result = null;
        if (metrics1 instanceof HashMap) {
            Map<String, Number> map1 = (HashMap<String, Number>) metrics1;
            Map<String, Number> map2 = (HashMap<String, Number>) metrics2;
            result = new HashMap<String, Number>();
            for (String key : map1.keySet()) {
                Number value1 = map1.get(key);
                Number value2 = map2.get(key);
                ((HashMap) result).put(key, Numbers.multiply(value1, value2));
            }

        } else if (metrics1 instanceof Number) {
            Number value1 = (Number) metrics1;
            Number value2 = (Number) metrics2;
            result = Numbers.multiply(value1, value2);
        }
        logMetric(metrics1, metrics2, result, "*");
        return result;
    }

    public static Object div(Object metrics1, Object metrics2) throws IllegalArgumentException {
        checkMetrics(metrics1, metrics2);
        Object result = null;
        if (metrics1 instanceof HashMap) {
            Map<String, Number> map1 = (HashMap<String, Number>) metrics1;
            Map<String, Number> map2 = (HashMap<String, Number>) metrics2;
            result = new HashMap<String, Number>();
            for (String key : map1.keySet()) {
                Number value1 = map1.get(key);
                Number value2 = map2.get(key);
                if(value1 == null || value2 == null){
                    log.warn("value2 is null, and value1 is {}",value1);
                    ((HashMap) result).put(key, null);
                }else if(Numbers.equiv(value2,0)){
                    log.warn("value2 is zero, and value1 is {}",value1);
                    ((HashMap) result).put(key, 0.0);
                }else{
                    ((HashMap) result).put(key, value1.doubleValue() / value2.doubleValue());
                }

            }

        } else if (metrics1 instanceof Number) {
            Number value1 = (Number) metrics1;
            Number value2 = (Number) metrics2;
            if(Numbers.equiv(value2,0)){
                log.warn("value2 is zero, and value1 is {}",value1);
                result = 0.0;
            }else{
                result = value1.doubleValue() / value2.doubleValue();
            }

        }
        logMetric(metrics1, metrics2, result, "/");
        return result;
    }

    public static Object union(Object metrics1, Object metrics2) throws IllegalArgumentException {
        if (metrics1 instanceof HashMap && metrics2 instanceof HashMap) {
            Map<String, Number> resultMap = new HashMap<>();
            Map<String, Number> map1 = (HashMap<String, Number>) metrics1;
            Map<String, Number> map2 = (HashMap<String, Number>) metrics2;
            resultMap.putAll(map1);
            resultMap.putAll(map2);
            return resultMap;
        } else {
            throw new ExpressionExecuteException("metrics type " + metrics1.getClass() + " is invalid");
        }
    }


    public static Object defaultOperation(Object value1, Object value2) throws IllegalArgumentException {
        if (value1 instanceof HashMap && value2 instanceof HashMap) {
            HashMap<String, Number> map1 = (HashMap) value1;
            HashMap<String, Number> map2 = (HashMap) value2;
            map1.forEach((k, v) -> {
                if (Numbers.equiv(map1.get(k), 0)) {
                    if (map2.get(k).longValue() > 0) {
                        map1.put(k, 0L);
                    } else {
                        map1.put(k, null);
                    }
                }
            });
            return value1;
        } else if (value1 instanceof Number && value2 instanceof Number) {

            if (Numbers.gt(value1, 0)) {
                return value1;
            } else {
                if (Numbers.gt(value2, 0)) {
                    return 0l;
                } else {
                    return null;
                }
            }
        } else {
            throw new ExpressionExecuteException("invalid arguments value,arg1:" + value1 + ",arg2:" + value2);
        }
    }


    public static void checkMetrics(Object metrics1, Object metrics2) {
        Preconditions.batchCheckNotNull(metrics1, metrics2);
        Preconditions.checkArgument((metrics1 instanceof Map && metrics2 instanceof Map) || (metrics1 instanceof Number && metrics2 instanceof Number), "both metrics1 and metrics2 are not number or map!");
        if (metrics1 instanceof HashMap) {
            Map<String, Number> map1 = (HashMap<String, Number>) metrics1;
            Map<String, Number> map2 = (HashMap<String, Number>) metrics2;
            Set<String> keys1 = new HashSet<>(map1.keySet());
            Set<String> keys2 = new HashSet<>(map2.keySet());

            if (!keys1.equals(keys2)) {
                throw new IllegalArgumentException("Keys are not the same in both maps");
            }
        } else if (metrics1 instanceof Number) {
            //
        } else {
            throw new ExpressionExecuteException("metrics type " + metrics1.getClass() + " is invalid!");
        }

    }

    private static void logMetric(Object metrics1, Object metrics2, Object result, String method) {
        log.info("{} {} {} = {}", metrics1, method, metrics2, result);
    }


    public static Map<String, Number> slideAgg(Map<String, Number> data, int windowSize) {
        return slideAgg(data, windowSize,1);
    }

    public static Map<String, Number> slideAgg(Map<String, Number> data, int windowSize,int stepSize) {
        Map<String, Number> result = new HashMap<>();
        if (data == null || data.size() == 0) {
            return result;
        }

        TreeMap<Integer, Number> sortedData = new TreeMap<>();
        for (Map.Entry<String, Number> entry : data.entrySet()) {
            sortedData.put(Integer.parseInt(entry.getKey()), entry.getValue());
        }
        Integer maxWeek = sortedData.lastKey();

        int mapIndex = 0;
        for (Map.Entry<Integer, Number> entry : sortedData.entrySet()) {
            if (mapIndex++ % stepSize != 0){
                continue;
            }
            Number sum = entry.getValue();
            int currentWeek = entry.getKey();
            int endWeek = currentWeek;

            int count = 1;
            int nextWeek = currentWeek + 1;
            while (count < windowSize && nextWeek <= maxWeek) {
                if (sortedData.containsKey(nextWeek)) {
                    sum = Numbers.add(sum, sortedData.get(nextWeek));
                    count++;
                    endWeek = nextWeek;
                }
                nextWeek++;
            }
            if (count == windowSize || nextWeek > maxWeek) {
                result.put(currentWeek+"___"+endWeek, sum);
            }
            if(nextWeek > maxWeek)
                break;
        }

        return result;
    }
}
