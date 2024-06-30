package org.soloquest.soloscan.compiler.lexer.token;

import java.util.Map;


public interface Token<T> {
    Token<T> withMeta(String name, Object v);

    Map<String, Object> getMetaMap();

    <V> V getMeta(final String name, final V defaultVal);

    <V> V getMeta(final String name);

    T getJavaValue(Map<String, Object> env);

    TokenType getType();

    String getLexeme();

    int getStartIndex();

    int getLineNo();

    enum TokenType {
        String, Variable, Number, Char, Operator, Delegate
    }
}
