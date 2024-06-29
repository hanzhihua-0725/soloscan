package org.soloquest.soloscan.compiler.codegen;

import org.soloquest.soloscan.compiler.lexer.token.Token;
import org.soloquest.soloscan.compiler.parser.Parser;

public interface CodeGenerator {

    void setParser(Parser parser);

    void onAdd(Token<?> lookhead);


    void onIn(Token<?> lookhead);

    void onSub(Token<?> lookhead);


    void onMult(Token<?> lookhead);

    void onDiv(Token<?> lookhead);


    void onAndLeft(Token<?> lookhead);


    void onAndRight(Token<?> lookhead);


    void onJoinLeft(Token<?> lookhead);


    void onJoinRight(Token<?> lookhead);


    void onEq(Token<?> lookhead);


    void onNeq(Token<?> lookhead);


    void onLt(Token<?> lookhead);


    void onLe(Token<?> lookhead);


    void onGt(Token<?> lookhead);


    void onGe(Token<?> lookhead);


    void onMod(Token<?> lookhead);


    void onNot(Token<?> lookhead);


    void onNeg(Token<?> lookhead);

    Object getResult();

    void onConstant(Token<?> lookhead);

    void onMethodName(Token<?> lookhead);

    void onMethodParameter(Token<?> lookhead);

    void onMethodInvoke(Token<?> lookhead);

    void onGroupingEnd(Token<?> lookhead);

    void onDefaultOperation(Token<?> lookhead);

    void onUnion(Token<?> lookhead);
}
