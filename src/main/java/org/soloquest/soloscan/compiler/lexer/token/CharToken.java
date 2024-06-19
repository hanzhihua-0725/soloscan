package org.soloquest.soloscan.compiler.lexer.token;

import java.util.Map;

public class CharToken extends AbstractToken<Character> {

    private final char ch;

    public char getCh() {
        return this.ch;
    }


    public CharToken(final char peek, final int lineNo, final int startIndex) {
        super(String.valueOf(peek), lineNo, startIndex);
        this.ch = peek;
    }


    @Override
    public TokenType getType() {
        return TokenType.Char;
    }


    @Override
    public Character getJavaValue(final Map<String, Object> env) {
        return this.ch;
    }


}
