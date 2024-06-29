package org.soloquest.soloscan.compiler.lexer.token;


import org.soloquest.soloscan.exception.ExpressionExecuteException;
import org.soloquest.soloscan.runtime.lang.SBoolean;
import org.soloquest.soloscan.runtime.lang.SObject;

import java.util.Map;

public enum OperatorType {

    NOT("!", 1),

    MULT("*", 2),

    Exponent("**", 2),

    DIV("/", 2),

    MOD("%", 2),

    ADD("+", 2),

    SUB("-sub", 2),

    LT("<", 2),

    LE("<=", 2),

    GT(">", 2),

    GE(">=", 2),

    EQ("=", 2),

    NEQ("!=", 2),

    AND("&&", 2),

    OR("||", 2),

    FUNC("()", Integer.MAX_VALUE),

    NEG("-neg", 1),

    IN("in", 2),

    UNION("union", 2),

    DEFAULTOPERATION("default_operation", 2);

    public final String token;

    public final int arity;


    OperatorType(final String token, final int operandCount) {
        this.token = token;
        this.arity = operandCount;
    }


    public SObject eval(final SObject[] args, final Map<String, Object> env) {
        if (args.length < this.arity) {
            throw new ExpressionExecuteException("Expect " + this.arity + " parameters for " + name()
                    + ", but have " + args.length + " arguments.");
        }
        switch (this) {
            case ADD:
                return args[0].add(args[1], env);
            case SUB:
                return args[0].sub(args[1], env);
            case MOD:
                return args[0].mod(args[1], env);
            case DIV:
                return args[0].div(args[1], env);
            case MULT:
                return args[0].mult(args[1], env);
            case EQ:
                int result = args[0].compare(args[1], env);
                return SBoolean.valueOf(result == 0);
            case NEQ:
                result = args[0].compare(args[1], env);
                return SBoolean.valueOf(result != 0);
            case LT:
                result = args[0].compare(args[1], env);
                return SBoolean.valueOf(result < 0);
            case LE:
                result = args[0].compare(args[1], env);
                return SBoolean.valueOf(result <= 0);
            case GT:
                result = args[0].compare(args[1], env);
                return SBoolean.valueOf(result > 0);
            case GE:
                result = args[0].compare(args[1], env);
                return SBoolean.valueOf(result >= 0);
            case NOT:
                return args[0].not(env);
            case NEG:
                return args[0].neg(env);
            case AND:
                return (args[0].booleanValue(env) && args[1].booleanValue(env)) ? SBoolean.TRUE
                        : SBoolean.FALSE;
            case OR:
                return (args[0].booleanValue(env) || args[1].booleanValue(env)) ? SBoolean.TRUE
                        : SBoolean.FALSE;
            case IN:
                return args[0].in(args[1], env);
            case FUNC:
                break;


        }
        return null;
    }


    public String getToken() {
        return this.token;
    }


    public int getArity() {
        return this.arity;
    }
}
