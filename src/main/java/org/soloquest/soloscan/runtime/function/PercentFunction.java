package org.soloquest.soloscan.runtime.function;

import org.soloquest.soloscan.runtime.lang.*;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.stream.Collectors;

public class PercentFunction extends AbstractFunction {

    private final static String DEFAULT_PERCENT_PATTERN_STRING = "##0.00%";
    private final static SString DEFAULT_PERCENT_PATTERN = new SString(DEFAULT_PERCENT_PATTERN_STRING);

    @Override
    public String getName() {
        return "percent";
    }

    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1) {
        return call(env, arg1, DEFAULT_PERCENT_PATTERN);
    }

    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1, final SObject arg2) {
        if ((arg1 instanceof SNumber || arg1 instanceof SMertric) && arg2 instanceof SString) {
            DecimalFormat decimalFormat = new DecimalFormat(((SString) arg2).getValue(env));
            if (arg1 instanceof SNumber) {
                Number number = (Number) ((SNumber) arg1).getValue(env);
                return SRuntimeJavaType.valueOf(decimalFormat.format(number.doubleValue()));
            }
            if (arg1 instanceof SMertric) {
                SMertric sMertric = (SMertric) arg1;
                Object value = sMertric.getValue(env);
                if (value instanceof Map) {
                    Map<String, Number> map = (Map<String, Number>) value;
                    Map<String, String> stringMap = map.entrySet().stream().collect(Collectors.toMap(
                            Map.Entry::getKey, entry -> decimalFormat.format(entry.getValue().doubleValue())
                    ));
                    return SRuntimeJavaType.valueOf(stringMap);
                } else if (value instanceof Number) {
                    Number number = (Number) value;
                    return SRuntimeJavaType.valueOf(decimalFormat.format(number.doubleValue()));
                }
            }
        }
        return super.call(env, arg1);
    }


}
