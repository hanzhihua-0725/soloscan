package org.soloquest.soloscan.compiler.codegen;

import org.soloquest.soloscan.compiler.lexer.token.DelegateToken;
import org.soloquest.soloscan.compiler.lexer.token.OperatorToken;
import org.soloquest.soloscan.compiler.lexer.token.OperatorType;
import org.soloquest.soloscan.compiler.lexer.token.Token;
import org.soloquest.soloscan.utils.Preconditions;

public abstract class AbstractCodeGenerator implements CodeGenerator, CodeConstants {

    protected TokenContainer merticsTokenContainer = new TokenContainer();
    protected TokenContainer groupingTokenContainer = new TokenContainer();
    protected TokenContainer filterTokenContainer = new TokenContainer();

    protected TokenContainer tokenContainer = merticsTokenContainer;

    public void switchGroupingPart() {
        tokenContainer = groupingTokenContainer;
    }

    public void switchFilterTokenContainer() {
        tokenContainer = filterTokenContainer;
    }

    @Override
    public void onAdd(final Token<?> lookhead) {
        this.tokenContainer.add(new OperatorToken(lookhead, OperatorType.ADD));

    }

    @Override
    public void onIn(final Token<?> lookhead) {
        this.tokenContainer.add(new OperatorToken(lookhead, OperatorType.IN));
    }


    @Override
    public void onAndLeft(final Token<?> lookhead) {
        this.tokenContainer.add(new DelegateToken(lookhead, DelegateToken.DelegateTokenType.And_Left));
    }


    @Override
    public void onAndRight(final Token<?> lookhead) {
        this.tokenContainer.add(new OperatorToken(lookhead, OperatorType.AND));

    }


    @Override
    public void onConstant(final Token<?> lookhead) {
        this.tokenContainer.add(lookhead);
    }


    @Override
    public void onDiv(final Token<?> lookhead) {
        this.tokenContainer.add(new OperatorToken(lookhead, OperatorType.DIV));

    }


    @Override
    public void onEq(final Token<?> lookhead) {
        this.tokenContainer.add(new OperatorToken(lookhead, OperatorType.EQ));

    }


    @Override
    public void onGe(final Token<?> lookhead) {
        this.tokenContainer.add(new OperatorToken(lookhead, OperatorType.GE));

    }


    @Override
    public void onGt(final Token<?> lookhead) {
        this.tokenContainer.add(new OperatorToken(lookhead, OperatorType.GT));

    }


    @Override
    public void onJoinLeft(final Token<?> lookhead) {
        this.tokenContainer.add(new DelegateToken(lookhead, DelegateToken.DelegateTokenType.Join_Left));
    }


    @Override
    public void onJoinRight(final Token<?> lookhead) {
        this.tokenContainer.add(new OperatorToken(lookhead, OperatorType.OR));

    }


    @Override
    public void onLe(final Token<?> lookhead) {
        this.tokenContainer.add(new OperatorToken(lookhead, OperatorType.LE));

    }


    @Override
    public void onLt(final Token<?> lookhead) {
        this.tokenContainer.add(new OperatorToken(lookhead, OperatorType.LT));

    }

    @Override
    public void onMethodInvoke(final Token<?> lookhead) {
        OperatorToken token = new OperatorToken(lookhead, OperatorType.FUNC);
        token.setMetaMap(lookhead != null ? lookhead.getMetaMap() : null);
        this.tokenContainer.add(token);

    }


    @Override
    public void onMethodName(final Token<?> lookhead) {
        this.tokenContainer.add(new DelegateToken(lookhead, DelegateToken.DelegateTokenType.Method_Name));

    }


    @Override
    public void onMethodParameter(final Token<?> lookhead) {
        this.tokenContainer.add(new DelegateToken(lookhead, DelegateToken.DelegateTokenType.Method_Param));

    }


    @Override
    public void onMod(final Token<?> lookhead) {
        this.tokenContainer.add(new OperatorToken(lookhead, OperatorType.MOD));

    }


    @Override
    public void onMult(final Token<?> lookhead) {
        this.tokenContainer.add(new OperatorToken(lookhead, OperatorType.MULT));

    }

    @Override
    public void onNeg(final Token<?> lookhead) {
        this.tokenContainer.add(new OperatorToken(lookhead, OperatorType.NEG));

    }


    @Override
    public void onNeq(final Token<?> lookhead) {
        this.tokenContainer.add(new OperatorToken(lookhead, OperatorType.NEQ));

    }


    @Override
    public void onNot(final Token<?> lookhead) {
        this.tokenContainer.add(new OperatorToken(lookhead, OperatorType.NOT));

    }


    @Override
    public void onSub(final Token<?> lookhead) {
        this.tokenContainer.add(new OperatorToken(lookhead, OperatorType.SUB));
    }


    @Override
    public void onGroupingEnd(Token<?> lookhead) {
        Preconditions.checkArgument(this.tokenContainer == this.groupingTokenContainer, "this method must be invoked in grouping part");
        this.tokenContainer.add(lookhead);
    }

    @Override
    public void onDefaultOperation(Token<?> lookhead) {
        this.tokenContainer.add(new OperatorToken(lookhead, OperatorType.DEFAULTOPERATION));
    }

    @Override
    public void onUnion(Token<?> lookhead) {
        this.tokenContainer.add(new OperatorToken(lookhead, OperatorType.UNION));
    }

    public boolean hasFilter() {
        return !this.filterTokenContainer.tokenList.isEmpty();
    }
}
