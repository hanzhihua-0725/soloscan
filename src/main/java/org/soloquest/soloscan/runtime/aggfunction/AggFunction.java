package org.soloquest.soloscan.runtime.aggfunction;

import org.soloquest.soloscan.compiler.lexer.token.Token;
import org.soloquest.soloscan.utils.Env;

import java.util.List;

public interface AggFunction {
    Number getValue();

    void setFilter(AggInner aggInner);

    AggInner getFilter();

    void process(Env env);

    String getPlaceHolder();

    default boolean validate(List<Token> filterTokenList) {
        return true;
    }

    default String getGroupBy() {
        return "ALL";
    }

}
