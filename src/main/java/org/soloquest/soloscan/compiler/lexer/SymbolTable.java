package org.soloquest.soloscan.compiler.lexer;

import org.soloquest.soloscan.compiler.lexer.token.Token;
import org.soloquest.soloscan.compiler.lexer.token.VariableToken;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

    private final Map<String, VariableToken> table = new HashMap<>();

    private static final Map<String, VariableToken> RESERVED = new HashMap<>();

    private static void reserveKeyword(final VariableToken v) {
        RESERVED.put(v.getLexeme(), v);
    }

    static {
        reserveKeyword(VariableToken.TRUE);
        reserveKeyword(VariableToken.FALSE);
        reserveKeyword(VariableToken.NULL);
    }

    public static boolean isReservedKeyword(final String name) {
        return RESERVED.containsKey(name);
    }

    public static boolean isReservedKeyword(final VariableToken v) {
        return isReservedKeyword(v.getLexeme());
    }


    public boolean isReserved(final String name) {
        return isReservedKeyword(name) || this.table.containsKey(name);
    }


    public VariableToken getVariable(final String name) {
        VariableToken var = RESERVED.get(name);
        return var != null ? var : this.table.get(name);
    }

    public VariableToken reserve(final String lexeme) {
        if (isReserved(lexeme)) {
            return getVariable(lexeme);
        } else {
            final VariableToken var = new VariableToken(lexeme, 0, -1);
            this.table.put(lexeme, var);
            return var;
        }
    }

    public Token<?> reserve(final VariableToken variableToken) {
        String lexeme = variableToken.getLexeme();
        if (isReserved(lexeme)) {
            if (isReservedKeyword(lexeme)) {
                return getVariable(lexeme);
            }
            return variableToken;
        } else {
            this.table.put(lexeme, variableToken);
            return variableToken;
        }
    }
}
