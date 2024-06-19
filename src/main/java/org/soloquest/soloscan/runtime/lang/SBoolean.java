package org.soloquest.soloscan.runtime.lang;

import java.util.Map;

public class SBoolean extends SObject<Boolean> {

    Boolean value;

    public static final SBoolean TRUE = new SBoolean(Boolean.TRUE);

    public static final SBoolean FALSE = new SBoolean(Boolean.FALSE);


    @Override
    public SObject not(final Map<String, Object> env) {
        return this.value.booleanValue() ? FALSE : TRUE;
    }


    @Override
    public SObject add(final SObject other, final Map<String, Object> env) {
        if (LangHelper.valueIsString(other, env)) {
            return new SString(this.value.toString() + other.getValue(env));
        }
        return super.add(other, env);

    }


    @Override
    public SObjectType getSObjectType() {
        return SObjectType.Boolean;
    }


    @Override
    public final Boolean getValue(final Map<String, Object> env) {
        return this.value;
    }


    private SBoolean(final Boolean value) {
        super();
        this.value = value;
    }


    public static SBoolean valueOf(final boolean b) {
        return b ? SBoolean.TRUE : SBoolean.FALSE;
    }


    @Override
    public int innerCompare(final SObject other, final Map<String, Object> env) {
        switch (other.getSObjectType()) {
            case Boolean:
                SBoolean otherBoolean = (SBoolean) other;
                return this.value.compareTo(otherBoolean.value);
            case JavaType:
                SJavaType javaType = (SJavaType) other;
                final Object otherValue = javaType.getValue(env);
                if (otherValue instanceof Boolean) {
                    return this.value.compareTo((Boolean) otherValue);
                } else {
                    throw new UnsupportedOperationException();
                }
            default:
                throw new UnsupportedOperationException();
        }

    }

}