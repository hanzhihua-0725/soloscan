package org.soloquest.soloscan.compiler.codegen;

import lombok.Getter;
import org.soloquest.soloscan.compiler.lexer.SymbolTable;
import org.soloquest.soloscan.compiler.lexer.token.Token;
import org.soloquest.soloscan.compiler.parser.Parser;
import org.soloquest.soloscan.utils.ParserUtils;

import java.util.ArrayList;
import java.util.List;

public class MockCodeGenerator extends AbstractCodeGenerator {

    @Getter
    private List<String> variables = new ArrayList<>();

    @Override
    public void setParser(Parser parser) {

    }

    @Override
    public Object getResult() {
        return null;
    }

    @Override
    public void onUnion(Token lookhead) {

    }

    @Override
    public void onDefaultOperation(Token lookhead) {

    }

    @Override
    public void onGroupingEnd(Token lookhead) {

    }

    @Override
    public void onMethodInvoke(Token lookhead) {

    }

    @Override
    public void onMethodParameter(Token lookhead) {

    }

    @Override
    public void onMethodName(Token lookhead) {

    }

    @Override
    public void onConstant(Token token) {
        if (ParserUtils.isVariableToken(token)) {
            String varName = token.getLexeme();
            if(!SymbolTable.isReservedKeyword(varName))
                variables.add(varName);
        }
    }

    @Override
    public void onNeg(Token lookhead) {

    }

    @Override
    public void onNot(Token lookhead) {

    }

    @Override
    public void onMod(Token lookhead) {

    }

    @Override
    public void onGe(Token lookhead) {

    }

    @Override
    public void onGt(Token lookhead) {

    }

    @Override
    public void onLe(Token lookhead) {

    }

    @Override
    public void onLt(Token lookhead) {

    }

    @Override
    public void onNeq(Token lookhead) {

    }

    @Override
    public void onEq(Token lookhead) {

    }

    @Override
    public void onJoinRight(Token lookhead) {

    }

    @Override
    public void onJoinLeft(Token lookhead) {

    }

    @Override
    public void onAndRight(Token lookhead) {

    }

    @Override
    public void onAndLeft(Token lookhead) {

    }

    @Override
    public void onDiv(Token lookhead) {

    }

    @Override
    public void onMult(Token lookhead) {

    }

    @Override
    public void onSub(Token lookhead) {

    }

    @Override
    public void onIn(Token lookhead) {

    }

    @Override
    public void onAdd(Token lookhead) {

    }
}
