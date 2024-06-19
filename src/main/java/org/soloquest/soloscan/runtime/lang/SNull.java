package org.soloquest.soloscan.runtime.lang;

import org.soloquest.soloscan.utils.TypeUtils;

import java.util.Map;

public class SNull extends SObject<Object> {
    public static final SNull NULL = new SNull();


    private SNull() {

    }


    @Override
    public SObject add(final SObject other, final Map<String, Object> env) {
        switch (other.getSObjectType()) {
            case String:
                return new SString("null" + other.getValue(env));
            case JavaType:
                final Object otherValue = other.getValue(env);
                if (TypeUtils.isString(otherValue)) {
                    return new SString("null" + otherValue);
                }
            default:
                return super.add(other, env);
        }
    }


    @Override
    public int innerCompare(final SObject other, final Map<String, Object> env) {
        switch (other.getSObjectType()) {
            case Nil:
                return 0;
            case JavaType:
                if (other.getValue(env) == null) {
                    return 0;
                }
        }
        return -1;
    }


    @Override
    public SObjectType getSObjectType() {
        return SObjectType.Nil;
    }


    @Override
    public Object getValue(final Map<String, Object> env) {
        return null;
    }

}
