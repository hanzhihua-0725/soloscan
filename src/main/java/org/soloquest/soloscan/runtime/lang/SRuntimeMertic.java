package org.soloquest.soloscan.runtime.lang;

import java.util.Map;

public class SRuntimeMertic extends SMertric {

    protected Object value;

    public SRuntimeMertic(final Object value) {
        super(null);
        this.value = value;
    }


    @Override
    public Object getValue(final Map<String, Object> env) {

        return this.value;
    }


}
