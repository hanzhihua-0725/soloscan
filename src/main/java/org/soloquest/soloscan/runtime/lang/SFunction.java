package org.soloquest.soloscan.runtime.lang;

import java.util.Map;
import java.util.concurrent.Callable;

public interface SFunction extends Callable<SObject>, Runnable {

    String getName();

    SObject call(Map<String, Object> env);


    public SObject call(Map<String, Object> env, SObject arg1);


    public SObject call(Map<String, Object> env, SObject arg1, SObject arg2);


    public SObject call(Map<String, Object> env, SObject arg1, SObject arg2,
                        SObject arg3);


    public SObject call(Map<String, Object> env, SObject arg1, SObject arg2,
                        SObject arg3, SObject arg4);


    public SObject call(Map<String, Object> env, SObject arg1, SObject arg2,
                        SObject arg3, SObject arg4, SObject arg5);


    public SObject call(Map<String, Object> env, SObject arg1, SObject arg2,
                        SObject arg3, SObject arg4, SObject arg5, SObject arg6);


    public SObject call(Map<String, Object> env, SObject arg1, SObject arg2,
                        SObject arg3, SObject arg4, SObject arg5, SObject arg6,
                        SObject arg7);


    public SObject call(Map<String, Object> env, SObject arg1, SObject arg2,
                        SObject arg3, SObject arg4, SObject arg5, SObject arg6,
                        SObject arg7, SObject arg8);


    public SObject call(Map<String, Object> env, SObject arg1, SObject arg2,
                        SObject arg3, SObject arg4, SObject arg5, SObject arg6,
                        SObject arg7, SObject arg8, SObject arg9);


    public SObject call(Map<String, Object> env, SObject arg1, SObject arg2,
                        SObject arg3, SObject arg4, SObject arg5, SObject arg6,
                        SObject arg7, SObject arg8, SObject arg9, SObject arg10);


    public SObject call(Map<String, Object> env, SObject arg1, SObject arg2,
                        SObject arg3, SObject arg4, SObject arg5, SObject arg6,
                        SObject arg7, SObject arg8, SObject arg9, SObject arg10,
                        SObject arg11);


    public SObject call(Map<String, Object> env, SObject arg1, SObject arg2,
                        SObject arg3, SObject arg4, SObject arg5, SObject arg6,
                        SObject arg7, SObject arg8, SObject arg9, SObject arg10,
                        SObject arg11, SObject arg12);


    public SObject call(Map<String, Object> env, SObject arg1, SObject arg2,
                        SObject arg3, SObject arg4, SObject arg5, SObject arg6,
                        SObject arg7, SObject arg8, SObject arg9, SObject arg10,
                        SObject arg11, SObject arg12, SObject arg13);


    public SObject call(Map<String, Object> env, SObject arg1, SObject arg2,
                        SObject arg3, SObject arg4, SObject arg5, SObject arg6,
                        SObject arg7, SObject arg8, SObject arg9, SObject arg10,
                        SObject arg11, SObject arg12, SObject arg13, SObject arg14);


    public SObject call(Map<String, Object> env, SObject arg1, SObject arg2,
                        SObject arg3, SObject arg4, SObject arg5, SObject arg6,
                        SObject arg7, SObject arg8, SObject arg9, SObject arg10,
                        SObject arg11, SObject arg12, SObject arg13, SObject arg14,
                        SObject arg15);


    public SObject call(Map<String, Object> env, SObject arg1, SObject arg2,
                        SObject arg3, SObject arg4, SObject arg5, SObject arg6,
                        SObject arg7, SObject arg8, SObject arg9, SObject arg10,
                        SObject arg11, SObject arg12, SObject arg13, SObject arg14,
                        SObject arg15, SObject arg16);


    public SObject call(Map<String, Object> env, SObject arg1, SObject arg2,
                        SObject arg3, SObject arg4, SObject arg5, SObject arg6,
                        SObject arg7, SObject arg8, SObject arg9, SObject arg10,
                        SObject arg11, SObject arg12, SObject arg13, SObject arg14,
                        SObject arg15, SObject arg16, SObject arg17);


    public SObject call(Map<String, Object> env, SObject arg1, SObject arg2,
                        SObject arg3, SObject arg4, SObject arg5, SObject arg6,
                        SObject arg7, SObject arg8, SObject arg9, SObject arg10,
                        SObject arg11, SObject arg12, SObject arg13, SObject arg14,
                        SObject arg15, SObject arg16, SObject arg17, SObject arg18);


    public SObject call(Map<String, Object> env, SObject arg1, SObject arg2,
                        SObject arg3, SObject arg4, SObject arg5, SObject arg6,
                        SObject arg7, SObject arg8, SObject arg9, SObject arg10,
                        SObject arg11, SObject arg12, SObject arg13, SObject arg14,
                        SObject arg15, SObject arg16, SObject arg17, SObject arg18,
                        SObject arg19);


    public SObject call(Map<String, Object> env, SObject arg1, SObject arg2,
                        SObject arg3, SObject arg4, SObject arg5, SObject arg6,
                        SObject arg7, SObject arg8, SObject arg9, SObject arg10,
                        SObject arg11, SObject arg12, SObject arg13, SObject arg14,
                        SObject arg15, SObject arg16, SObject arg17, SObject arg18,
                        SObject arg19, SObject arg20);


    public SObject call(Map<String, Object> env, SObject arg1, SObject arg2,
                        SObject arg3, SObject arg4, SObject arg5, SObject arg6,
                        SObject arg7, SObject arg8, SObject arg9, SObject arg10,
                        SObject arg11, SObject arg12, SObject arg13, SObject arg14,
                        SObject arg15, SObject arg16, SObject arg17, SObject arg18,
                        SObject arg19, SObject arg20, SObject... args);

}