package org.soloquest.soloscan.compiler.lexer.token;

import java.util.Map;

public class VariableToken extends AbstractToken<Object> {

    public static final VariableToken TRUE = new VariableToken("true", 0, -1) {

        @Override
        public Object getJavaValue(final Map<String, Object> env) {
            return true;
        }

    };

    public static final VariableToken FALSE = new VariableToken("false", 0, -1) {

        @Override
        public Object getJavaValue(final Map<String, Object> env) {
            return false;
        }

    };

    public static final VariableToken NULL = new VariableToken("null", 0, -1) {

        @Override
        public Object getJavaValue(final Map<String, Object> env) {
            return null;
        }

    };


    @Override
    public TokenType getType() {
        return TokenType.Variable;
    }


    @Override
    public Object getJavaValue(final Map<String, Object> env) {
        if (env != null) {
            return env.get(this.lexeme);
        } else {
            return null;
        }
    }

    public VariableToken(final String name, final int lineNo, final int startIndex) {
        super(name, lineNo, startIndex);
    }


    @Override
    public String toString() {
        String index = ",index=" + getStartIndex();
        if (getStartIndex() == -1) {
            index = "";
        }
        return "[type='variable',lexeme='" + getLexeme() + "'" + index + "]";
    }

}
