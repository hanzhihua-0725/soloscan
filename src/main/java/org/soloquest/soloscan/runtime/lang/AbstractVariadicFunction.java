package org.soloquest.soloscan.runtime.lang;

import org.soloquest.soloscan.utils.Env;

import java.util.Map;

public abstract class AbstractVariadicFunction extends AbstractFunction {


    @Override
    public SObject call() throws Exception {
        return this.call(Env.EMPTY_ENV);
    }

    @Override
    public void run() {
        this.call(Env.EMPTY_ENV);
    }


    @Override
    public String desc(final Map<String, Object> env) {
        return "<" + getSObjectType() + ", " + getName() + ">";
    }

    @Override
    public int innerCompare(final SObject other, final Map<String, Object> env) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SObject call(final Map<String, Object> env) {
        return variadicCall(env);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1) {
        return variadicCall(env, arg1);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2) {
        return variadicCall(env, arg1, arg2);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3) {
        return variadicCall(env, arg1, arg2, arg3);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4) {
        return variadicCall(env, arg1, arg2, arg3, arg4);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5) {
        return variadicCall(env, arg1, arg2, arg3, arg4, arg5);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5, final SObject arg6) {
        return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5, final SObject arg6, final SObject arg7) {
        return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5, final SObject arg6, final SObject arg7,
                        final SObject arg8) {
        return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5, final SObject arg6, final SObject arg7,
                        final SObject arg8, final SObject arg9) {
        return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5, final SObject arg6, final SObject arg7,
                        final SObject arg8, final SObject arg9, final SObject arg10) {
        return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5, final SObject arg6, final SObject arg7,
                        final SObject arg8, final SObject arg9, final SObject arg10,
                        final SObject arg11) {
        return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5, final SObject arg6, final SObject arg7,
                        final SObject arg8, final SObject arg9, final SObject arg10,
                        final SObject arg11, final SObject arg12) {
        return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11,
                arg12);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5, final SObject arg6, final SObject arg7,
                        final SObject arg8, final SObject arg9, final SObject arg10,
                        final SObject arg11, final SObject arg12, final SObject arg13) {
        return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11,
                arg12, arg13);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5, final SObject arg6, final SObject arg7,
                        final SObject arg8, final SObject arg9, final SObject arg10,
                        final SObject arg11, final SObject arg12, final SObject arg13,
                        final SObject arg14) {
        return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11,
                arg12, arg13, arg14);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5, final SObject arg6, final SObject arg7,
                        final SObject arg8, final SObject arg9, final SObject arg10,
                        final SObject arg11, final SObject arg12, final SObject arg13,
                        final SObject arg14, final SObject arg15) {
        return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11,
                arg12, arg13, arg14, arg15);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5, final SObject arg6, final SObject arg7,
                        final SObject arg8, final SObject arg9, final SObject arg10,
                        final SObject arg11, final SObject arg12, final SObject arg13,
                        final SObject arg14, final SObject arg15, final SObject arg16) {
        return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11,
                arg12, arg13, arg14, arg15, arg16);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5, final SObject arg6, final SObject arg7,
                        final SObject arg8, final SObject arg9, final SObject arg10,
                        final SObject arg11, final SObject arg12, final SObject arg13,
                        final SObject arg14, final SObject arg15, final SObject arg16,
                        final SObject arg17) {
        return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11,
                arg12, arg13, arg14, arg15, arg16, arg17);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5, final SObject arg6, final SObject arg7,
                        final SObject arg8, final SObject arg9, final SObject arg10,
                        final SObject arg11, final SObject arg12, final SObject arg13,
                        final SObject arg14, final SObject arg15, final SObject arg16,
                        final SObject arg17, final SObject arg18) {
        return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11,
                arg12, arg13, arg14, arg15, arg16, arg17, arg18);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5, final SObject arg6, final SObject arg7,
                        final SObject arg8, final SObject arg9, final SObject arg10,
                        final SObject arg11, final SObject arg12, final SObject arg13,
                        final SObject arg14, final SObject arg15, final SObject arg16,
                        final SObject arg17, final SObject arg18, final SObject arg19) {
        return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11,
                arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5, final SObject arg6, final SObject arg7,
                        final SObject arg8, final SObject arg9, final SObject arg10,
                        final SObject arg11, final SObject arg12, final SObject arg13,
                        final SObject arg14, final SObject arg15, final SObject arg16,
                        final SObject arg17, final SObject arg18, final SObject arg19,
                        final SObject arg20) {
        return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11,
                arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5, final SObject arg6, final SObject arg7,
                        final SObject arg8, final SObject arg9, final SObject arg10,
                        final SObject arg11, final SObject arg12, final SObject arg13,
                        final SObject arg14, final SObject arg15, final SObject arg16,
                        final SObject arg17, final SObject arg18, final SObject arg19,
                        final SObject arg20, final SObject... args) {
        if (args == null || args.length == 0) {
            return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11,
                    arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20);
        } else {
            SObject[] allArgs = new SObject[20 + args.length];
            allArgs[0] = arg1;
            allArgs[1] = arg2;
            allArgs[2] = arg3;
            allArgs[3] = arg4;
            allArgs[4] = arg5;
            allArgs[5] = arg6;
            allArgs[6] = arg7;
            allArgs[7] = arg8;
            allArgs[8] = arg9;
            allArgs[9] = arg10;
            allArgs[10] = arg11;
            allArgs[11] = arg12;
            allArgs[12] = arg13;
            allArgs[13] = arg14;
            allArgs[14] = arg15;
            allArgs[15] = arg16;
            allArgs[16] = arg17;
            allArgs[17] = arg18;
            allArgs[18] = arg19;
            allArgs[19] = arg20;
            System.arraycopy(args, 0, allArgs, 20, args.length);
            return variadicCall(env, allArgs);
        }
    }


    public abstract SObject variadicCall(Map<String, Object> env, SObject... args);

}
