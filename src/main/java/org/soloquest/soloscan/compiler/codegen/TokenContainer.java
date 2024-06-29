package org.soloquest.soloscan.compiler.codegen;

import lombok.Getter;
import org.soloquest.soloscan.SoloscanOptions;
import org.soloquest.soloscan.compiler.lexer.SymbolTable;
import org.soloquest.soloscan.compiler.lexer.token.DelegateToken;
import org.soloquest.soloscan.compiler.lexer.token.Token;
import org.soloquest.soloscan.compiler.lexer.token.VariableToken;
import org.soloquest.soloscan.utils.ParserUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class TokenContainer {

    final List<Token<?>> tokenList = new ArrayList<>();

    @Getter
    protected Set<String> methodTokens = new HashSet<>();
    @Getter
    protected Set<String> variables = new HashSet<>();
    @Getter
    protected Set<String> metricsVariables = new HashSet<>();
    @Getter
    protected Set<Token<?>> constants = new HashSet<>();

    public List<Token<?>> getTokenList() {
        return this.tokenList;
    }


    public boolean add(Token<?> token) {
        processToken(token);
        return tokenList.add(token);
    }

    private void processToken(final Token<?> token) {
        switch (token.getType()) {
            case Number:
            case String:
                constants.add(token);
                break;
            case Variable:
                if (SymbolTable.isReservedKeyword((VariableToken) token)) {
                    return;
                }
                String varName = token.getLexeme();
                if (SoloscanOptions.getOption(SoloscanOptions.COLUMN_CASE_INSENSITIVE)) {
                    varName = varName.toLowerCase();
                }
                if (ParserUtils.isMetricToken(token)) {
                    metricsVariables.add(varName);
                } else {
                    variables.add(varName);
                }
                break;
            case Delegate:
                DelegateToken delegateToken = (DelegateToken) token;
                if (delegateToken.getDelegateTokenType() == DelegateToken.DelegateTokenType.Method_Name) {
                    Token<?> realToken = delegateToken.getToken();
                    if (realToken == null) {
                        return;
                    }
                    if (realToken.getType() == Token.TokenType.Variable) {
                        String methodName = token.getLexeme();
                        methodTokens.add(methodName);
                    }
                }
                break;
        }
    }

}
