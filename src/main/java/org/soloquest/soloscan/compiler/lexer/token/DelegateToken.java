package org.soloquest.soloscan.compiler.lexer.token;


import java.util.Map;


public class DelegateToken extends AbstractToken<Token<?>> {


    private static final long serialVersionUID = -1564600597069979843L;
    private final Token<?> token;
    private final DelegateTokenType delegateTokenType;


    public static enum DelegateTokenType {
        And_Left, Join_Left,
        Method_Name, Method_Param
    }


    public Token<?> getToken() {
        return this.token;
    }


    public DelegateTokenType getDelegateTokenType() {
        return this.delegateTokenType;
    }


    public DelegateToken(final Token<?> token, final DelegateTokenType type) {
        super(token != null ? token.getLexeme() : "", token != null ? token.getLineNo() : 0,
                token != null ? token.getStartIndex() : -1);
        this.token = token;
        this.delegateTokenType = type;
        if (token != null) {
            setMetaMap(token.getMetaMap());
        }
    }


    @Override
    public TokenType getType() {
        return TokenType.Delegate;
    }


    @Override
    public Token<?> getJavaValue(final Map<String, Object> env) {
        return this.token;
    }

    public String childInfo() {
        return "delegateTokenType:" + delegateTokenType;
    }

}
