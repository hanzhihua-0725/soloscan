package org.soloquest.soloscan.compiler.lexer.token;

import java.util.Map;


public class OperatorToken extends AbstractToken<OperatorType> {

    private final OperatorType operatorType;

    public OperatorToken(final Token<?> lookhead, final OperatorType operatorType) {
        super(operatorType.getToken(), lookhead != null ? lookhead.getLineNo() : 0,
                lookhead != null ? lookhead.getStartIndex() : -1);
        setMetaMap(lookhead != null ? lookhead.getMetaMap() : null);
        this.operatorType = operatorType;
    }

    public OperatorType getOperatorType() {
        return this.operatorType;
    }

    @Override
    public TokenType getType() {
        return TokenType.Operator;
    }


    @Override
    public OperatorType getJavaValue(final Map<String, Object> env) {
        return this.operatorType;
    }

}
