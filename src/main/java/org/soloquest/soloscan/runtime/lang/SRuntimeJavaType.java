package org.soloquest.soloscan.runtime.lang;

import java.util.Map;

public class SRuntimeJavaType extends SJavaType {

    protected Object object;

    public SRuntimeJavaType(final Object object) {
        super(null);
        this.object = object;
    }

    public static SObject valueOf(final Object object) {
        if (object == null) {
            return SNull.NULL;
        }
        if (object instanceof SObject) {
            return (SObject) object;
        }
        return new SRuntimeJavaType(object);
    }

    @Override
    public Object getValue(final Map<String, Object> env) {

        return this.object;
    }

}

