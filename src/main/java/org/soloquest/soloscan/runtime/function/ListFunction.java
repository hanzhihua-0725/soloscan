package org.soloquest.soloscan.runtime.function;

import org.soloquest.soloscan.runtime.lang.AbstractVariadicFunction;
import org.soloquest.soloscan.runtime.lang.SObject;
import org.soloquest.soloscan.runtime.lang.SRuntimeJavaType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListFunction extends AbstractVariadicFunction {


    @Override
    public String getName() {
        return "[]";
    }

    @Override
    public SObject variadicCall(final Map<String, Object> env, final SObject... args) {
        List<Object> list = new ArrayList<>(args != null ? args.length : 10);
        if (args != null) {
            for (SObject obj : args) {
                Object value = obj.getValue(env);
                list.add(value);
            }
        }

        return SRuntimeJavaType.valueOf(list);
    }


}