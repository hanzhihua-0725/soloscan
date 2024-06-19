package org.soloquest.soloscan.runtime.lang;

import java.util.Map;

public class LangHelper {

    public static boolean valueIsString(SObject sObject, Map<String, Object> env) {
        if (sObject instanceof SString) {
            return true;
        }
        Object o = sObject.getValue(env);
        return o instanceof String || o instanceof Character;
    }

    public static boolean eitherIsString(SObject first, SObject second, Map<String, Object> env) {
        return valueIsString(first, env) || valueIsString(second, env);
    }

    public static boolean bothAreNumber(SObject first, SObject second, Map<String, Object> env) {
        return isNumber(first, env) && isNumber(second, env);
    }

    public static boolean isNumber(SObject sObject, Map<String, Object> env) {
        if (sObject instanceof SNumber) {
            return true;
        }
        if (sObject instanceof SJavaType) {
            return ((SJavaType) sObject).getValue(env) instanceof Number;
        }
        return false;
    }
}
