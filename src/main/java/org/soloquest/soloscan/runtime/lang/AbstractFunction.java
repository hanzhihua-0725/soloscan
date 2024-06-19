package org.soloquest.soloscan.runtime.lang;

import org.soloquest.soloscan.utils.Env;

import java.util.Map;

public abstract class AbstractFunction extends SObject<SFunction> implements SFunction {
    @Override
    public SObject call() throws Exception {
        return this.call(Env.EMPTY_ENV);
    }

    @Override
    public void run() {
        this.call(Env.EMPTY_ENV);
    }

    public SObject throwArity(final int n) {
        String name = getName();
        throw new IllegalArgumentException("Wrong number of args (" + n + ") passed to: " + name);
    }

    @Override
    public String desc(final Map<String, Object> env) {
        return "<" + getSObjectType() + ", " + getName() + ">";
    }

    @Override
    public SObject call(final Map<String, Object> env) {
        return throwArity(0);
    }


    @Override
    public SObjectType getSObjectType() {
        return SObjectType.Method;
    }

    @Override
    public SFunction getValue(final Map<String, Object> env) {
        return this;
    }

    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1) {
        return throwArity(1);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2) {
        return throwArity(2);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3) {
        return throwArity(3);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4) {
        return throwArity(4);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5) {
        return throwArity(5);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5, final SObject arg6) {
        return throwArity(6);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5, final SObject arg6, final SObject arg7) {
        return throwArity(7);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5, final SObject arg6, final SObject arg7,
                        final SObject arg8) {
        return throwArity(8);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5, final SObject arg6, final SObject arg7,
                        final SObject arg8, final SObject arg9) {
        return throwArity(9);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5, final SObject arg6, final SObject arg7,
                        final SObject arg8, final SObject arg9, final SObject arg10) {
        return throwArity(10);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5, final SObject arg6, final SObject arg7,
                        final SObject arg8, final SObject arg9, final SObject arg10,
                        final SObject arg11) {
        return throwArity(11);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5, final SObject arg6, final SObject arg7,
                        final SObject arg8, final SObject arg9, final SObject arg10,
                        final SObject arg11, final SObject arg12) {
        return throwArity(12);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5, final SObject arg6, final SObject arg7,
                        final SObject arg8, final SObject arg9, final SObject arg10,
                        final SObject arg11, final SObject arg12, final SObject arg13) {
        return throwArity(13);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5, final SObject arg6, final SObject arg7,
                        final SObject arg8, final SObject arg9, final SObject arg10,
                        final SObject arg11, final SObject arg12, final SObject arg13,
                        final SObject arg14) {
        return throwArity(14);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5, final SObject arg6, final SObject arg7,
                        final SObject arg8, final SObject arg9, final SObject arg10,
                        final SObject arg11, final SObject arg12, final SObject arg13,
                        final SObject arg14, final SObject arg15) {
        return throwArity(15);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5, final SObject arg6, final SObject arg7,
                        final SObject arg8, final SObject arg9, final SObject arg10,
                        final SObject arg11, final SObject arg12, final SObject arg13,
                        final SObject arg14, final SObject arg15, final SObject arg16) {
        return throwArity(16);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5, final SObject arg6, final SObject arg7,
                        final SObject arg8, final SObject arg9, final SObject arg10,
                        final SObject arg11, final SObject arg12, final SObject arg13,
                        final SObject arg14, final SObject arg15, final SObject arg16,
                        final SObject arg17) {
        return throwArity(17);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5, final SObject arg6, final SObject arg7,
                        final SObject arg8, final SObject arg9, final SObject arg10,
                        final SObject arg11, final SObject arg12, final SObject arg13,
                        final SObject arg14, final SObject arg15, final SObject arg16,
                        final SObject arg17, final SObject arg18) {
        return throwArity(18);
    }


    @Override
    public SObject call(final Map<String, Object> env, final SObject arg1,
                        final SObject arg2, final SObject arg3, final SObject arg4,
                        final SObject arg5, final SObject arg6, final SObject arg7,
                        final SObject arg8, final SObject arg9, final SObject arg10,
                        final SObject arg11, final SObject arg12, final SObject arg13,
                        final SObject arg14, final SObject arg15, final SObject arg16,
                        final SObject arg17, final SObject arg18, final SObject arg19) {
        return throwArity(19);
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
        return throwArity(20);
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
        return throwArity(21);
    }

    public int innerCompare(SObject other, Map<String, Object> env) {
        throw new UnsupportedOperationException();
    }

}

