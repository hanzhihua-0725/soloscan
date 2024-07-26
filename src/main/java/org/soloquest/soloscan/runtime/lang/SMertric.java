package org.soloquest.soloscan.runtime.lang;

import lombok.extern.slf4j.Slf4j;
import org.soloquest.soloscan.compiler.lexer.SymbolTable;
import org.soloquest.soloscan.exception.ExpressionExecuteException;
import org.soloquest.soloscan.utils.Env;
import org.soloquest.soloscan.utils.MetricUtils;

import java.util.Map;

@Slf4j
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
        Object object =  env.get(this.name);
        if(object == null ){
            log.error("{} 's value is null",this.name);
            if(env instanceof Env){
                log.error("key:{},env:{}",this.name,((Env)env).getDetail());
            }else{
                log.error("key:{},env:{} not ENV",this.name,env);
            }
        }
        return object;
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
                if(otherValue == null){
                    return new SRuntimeMertic(null);
                }
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
