package org.soloquest.soloscan.runtime.lang;

import lombok.extern.slf4j.Slf4j;
import org.soloquest.soloscan.compiler.lexer.SymbolTable;
import org.soloquest.soloscan.runtime.function.IntRange;

import java.util.List;
import java.util.Map;

@Slf4j
public class SJavaType extends SObject<Object> {

    protected String name;

    @Override
    public SObjectType getSObjectType() {
        return SObjectType.JavaType;
    }

    public String getName() {
        return this.name;
    }

    public SJavaType(final String name) {
        this(name, null);
    }

    public SJavaType(final String name, final SymbolTable symbolTable) {
        super();
        if (name != null) {
            if (symbolTable != null) {
                this.name = symbolTable.reserve(name).getLexeme();
            } else {
                this.name = name;
            }
        }

    }


    @Override
    public Object getValue(final Map<String, Object> env) {
        Object value =  env.get(name);
        if(value == null){
            log.warn("env name:{} value is null",name);
        }
        return value;
    }


    @Override
    public SObject not(final Map<String, Object> env) {
        final Object value = getValue(env);
        if (value instanceof Boolean) {
            return SBoolean.valueOf((Boolean) value).not(env);
        } else {
            return super.not(env);
        }
    }

    public SObject in(final SObject other, final Map<String, Object> env) {
        Object value = getValue(env);
        if (value == null) {
            return SBoolean.valueOf(false);
        }
        switch (other.getSObjectType()) {
            case JavaType:
                SJavaType otherJavaType = (SJavaType) other;
                final Object otherValue = otherJavaType.getValue(env);
                if (otherValue instanceof List) {
                    if (value instanceof Number) {
                        value = new Long(((Number) value).intValue());
                        return SBoolean.valueOf(((List) otherValue).contains(value));
                    }
                } else if (otherValue instanceof IntRange) {
                    IntRange range = (IntRange) otherValue;
                    if (value instanceof Number) {
                        int v = ((Number) value).intValue();
                        return SBoolean.valueOf(range.in(v));
                    }
                }
            default:
                return super.in(other, env);
        }
    }

}
