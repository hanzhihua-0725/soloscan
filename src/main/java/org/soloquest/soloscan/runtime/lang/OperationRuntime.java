package org.soloquest.soloscan.runtime.lang;

import org.soloquest.soloscan.compiler.lexer.token.OperatorType;
import org.soloquest.soloscan.compiler.lexer.token.VariableToken;

import java.util.Map;

public class OperationRuntime {

    private static final ThreadLocal<SObject[]> TWO_ARRGS = new ThreadLocal<SObject[]>() {

        @Override
        protected SObject[] initialValue() {
            return new SObject[2];
        }

    };

    private static final ThreadLocal<SObject[]> ONE_ARG = new ThreadLocal<SObject[]>() {

        @Override
        protected SObject[] initialValue() {
            return new SObject[1];
        }

    };

    public static SObject eval(final Map<String, Object> env, final SObject[] args,
                               final OperatorType opType) {
        SObject ret = opType.eval(args, env);
        if (RuntimeUtils.isTracedEval(env)) {
            trace(env, opType, ret, args);
        }
        return ret;
    }


    public static SObject eval(final SObject left, final Map<String, Object> env, final SObject right,
                               final OperatorType opType) {

        SObject[] args = TWO_ARRGS.get();
        args[0] = left;
        args[1] = right;
        return opType.eval(args, env);
    }

    public static SObject eval(final SObject left, final SObject right,
                               final Map<String, Object> env, final OperatorType opType) {

        SObject[] args = TWO_ARRGS.get();
        args[0] = left;
        args[1] = right;
        return opType.eval(args, env);
    }

    private static final String WHITE_SPACE = " ";
    private static final String TRACE_PREFIX = "         ";

    private static String desc(final SObject arg, final Map<String, Object> env) {
        if (arg != null) {
            return arg.desc(env);
        } else {
            return VariableToken.NULL.getLexeme();
        }
    }

    private static void trace(final Map<String, Object> env, final OperatorType opType,
                              final SObject result, final SObject... args) {

        StringBuilder argsDec = new StringBuilder();
        argsDec.append(desc(args[0], env));
        for (int i = 1; i < args.length; i++) {
            if (args[i] != null) {
                argsDec.append(WHITE_SPACE).append(opType.token).append(WHITE_SPACE)
                        .append(desc(args[i], env));
            }
        }

//        RuntimeUtils.printlnTrace(env, TRACE_PREFIX + argsDec + " => " + desc(result, env));
    }
}
