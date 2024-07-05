package org.soloquest.soloscan.utils;

import org.soloquest.soloscan.compiler.lexer.token.Token;
import org.soloquest.soloscan.compiler.lexer.token.VariableToken;

public class ParserUtils {

    public static boolean isConstant(final Token<?> token) {
        if (token == null)
            return false;
        switch (token.getType()) {
            case Number:
            case String:
            case Char:
                return true;
            default:
                return false;
        }
    }

    public static final boolean isJavaIdentifier(final String id) {
        if (id == null) {
            return false;
        }

        if (id.equals("")) {
            return false;
        }

        if (!Character.isJavaIdentifierStart(id.charAt(0))) {
            return false;
        }

        for (int i = 1; i < id.length(); i++) {
            if (!Character.isJavaIdentifierPart(id.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static final boolean isMetricToken(Token t) {
        return t instanceof VariableToken && t.getMeta("IS_METRIC") instanceof Boolean && ((Boolean) t.getMeta("IS_METRIC")).booleanValue();
    }

    public static final boolean isVariableToken(Token token) {
        if (token instanceof VariableToken) {
            String name = token.getLexeme();
            if (!name.startsWith("ph_agg") && !name.startsWith("ph_mu")) {
                return true;
            }
        }
        return false;
    }

}
