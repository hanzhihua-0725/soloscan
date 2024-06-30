package org.soloquest.soloscan.runtime.lang;

import org.soloquest.soloscan.compiler.lexer.SymbolTable;
import org.soloquest.soloscan.exception.ExpressionExecuteException;
import org.soloquest.soloscan.utils.MetricUtils;

import java.util.Map;

public class SMertric extends SObject<Object> {

    protected String name;

    public SMertric(final String name) {
        this(name, null);
    }

    public SMertric(final String name, final SymbolTable symbolTable) {
        super();
        if (name != null) {
            if (symbolTable != null) {
                this.name = symbolTable.reserve(name).getLexeme();
            } else {
                this.name = name;
            }
        }
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int innerCompare(SObject other, Map<String, Object> env) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SObjectType getSObjectType() {
        return SObjectType.Metric;
    }

    @Override
    public Object getValue(Map<String, Object> env) {
        return env.get(this.name);
    }


    public SObject add(final SObject other, final Map<String, Object> env) {
        Object value = getValue(env);
        switch (other.getSObjectType()) {
            case Metric:
                Object otherValue = ((SMertric) other).getValue(env);
                return new SRuntimeMertic(MetricUtils.add(value, otherValue));
            default:
                throw new ExpressionExecuteException();
        }
    }

    public SObject sub(final SObject other, final Map<String, Object> env) {
        Object value = getValue(env);
        switch (other.getSObjectType()) {
            case Metric:
                Object otherValue = ((SMertric) other).getValue(env);
                return new SRuntimeMertic(MetricUtils.sub(value, otherValue));
            default:
                throw new ExpressionExecuteException();
        }
    }

    public SObject mult(final SObject other, final Map<String, Object> env) {
        Object value = getValue(env);
        switch (other.getSObjectType()) {
            case Metric:
                Object otherValue = ((SMertric) other).getValue(env);
                return new SRuntimeMertic(MetricUtils.mult(value, otherValue));
            default:
                throw new ExpressionExecuteException();
        }
    }

    public SObject div(final SObject other, final Map<String, Object> env) {
        Object value = getValue(env);
        switch (other.getSObjectType()) {
            case Metric:
                Object otherValue = ((SMertric) other).getValue(env);
                return new SRuntimeMertic(MetricUtils.div(value, otherValue));
            default:
                throw new ExpressionExecuteException();
        }
    }

    public SObject union(final SObject other, final Map<String, Object> env) {
        Object value = getValue(env);
        switch (other.getSObjectType()) {
            case Metric:
                Object otherValue = ((SMertric) other).getValue(env);
                return new SRuntimeMertic(MetricUtils.union(value, otherValue));
            default:
                throw new ExpressionExecuteException();
        }
    }

    public SObject defaultOperation(final SObject other, final Map<String, Object> env) {
        Object value = getValue(env);
        switch (other.getSObjectType()) {
            case Metric:
                Object otherValue = ((SMertric) other).getValue(env);
                return new SRuntimeMertic(MetricUtils.defaultOperation(value, otherValue));
            default:
                throw new ExpressionExecuteException();
        }
    }


}
