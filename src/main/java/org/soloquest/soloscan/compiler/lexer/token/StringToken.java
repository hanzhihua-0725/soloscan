package org.soloquest.soloscan.compiler.lexer.token;

import java.util.Map;

public class StringToken extends AbstractToken<String> {


    public StringToken(final String lexeme, final int lineNo, final int startIndex) {
        super(lexeme, lineNo, startIndex);
    }


    @Override
    public TokenType getType() {
        return TokenType.String;
    }


    @Override
    public String getJavaValue(final Map<String, Object> env) {
        return this.lexeme;
    }

}
