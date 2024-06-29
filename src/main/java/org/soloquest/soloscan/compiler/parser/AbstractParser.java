package org.soloquest.soloscan.compiler.parser;

import org.soloquest.soloscan.MetricUnitExpression;
import org.soloquest.soloscan.SoloscanExecutor;
import org.soloquest.soloscan.compiler.SoloscanCompileInterface;
import org.soloquest.soloscan.compiler.codegen.AbstractCodeGenerator;
import org.soloquest.soloscan.compiler.codegen.CodeGenerator;
import org.soloquest.soloscan.compiler.lexer.SoloscanLexer;
import org.soloquest.soloscan.compiler.lexer.SymbolTable;
import org.soloquest.soloscan.compiler.lexer.token.CharToken;
import org.soloquest.soloscan.compiler.lexer.token.Token;
import org.soloquest.soloscan.compiler.lexer.token.VariableToken;
import org.soloquest.soloscan.exception.ExpressionCompileException;
import org.soloquest.soloscan.utils.MiscUtils;

import java.util.ArrayDeque;

public class AbstractParser implements Parser {

    protected final SoloscanExecutor instance;
    protected final SoloscanLexer lexer;
    protected final CodeGenerator codeGenerator;

    protected SoloscanCompileInterface compiler;
    protected Token<?> lookhead;

    private final ArrayDeque<Token<?>> prevTokens = new ArrayDeque<>();

    public void setGlobalFilter(String globalFilter) {
        this.globalFilter = globalFilter;
    }

    protected String globalFilter;

    public AbstractParser(final SoloscanCompileInterface compiler, final SoloscanExecutor instance, final SoloscanLexer lexer,
                          final CodeGenerator codeGenerator) {
        this.compiler = compiler;
        this.instance = instance;
        this.lexer = lexer;
        this.codeGenerator = codeGenerator;
        this.lookhead = this.lexer.scan();

        if (this.lookhead == null) {
            reportCompileError("blank script");
        }
    }

    public void parseEntry() {
        parseLogicalOr();
    }


    protected void parseMetricPart() {
        parseEntry();
    }

    protected void parseFitlerPart() {
        ((AbstractCodeGenerator) this.codeGenerator).switchFilterTokenContainer();
        if (!MiscUtils.isBlank(globalFilter)) {
            lexer.appendString(" && " + globalFilter);
            globalFilter = null;
        }
        parseEntry();
    }

    protected void parseGroupingPart() {
        ((AbstractCodeGenerator) this.codeGenerator).switchGroupingPart();
        if (this.lookhead.getType() == Token.TokenType.Variable && this.lookhead.getLexeme().equalsIgnoreCase("grouping")) {
            move(true);
            if (expectChar('(')) {
                move(true);
                int paramCount = 0;
                do {
                    if (paramCount > 0) {
                        move(true);
                    }
                    if (this.lookhead.getType() == Token.TokenType.Variable) {
                        codeGenerator.onConstant(lookhead);
                        move(true);
                    } else if (expectChar(')')) {
                        return;
                    } else {
                        reportCompileError("invalid token");
                    }
                    paramCount++;
                } while (expectChar(','));
                if (expectChar(')')) {
                    move(true);
                } else {
                    reportCompileError("invalid token");
                }

            } else {
                reportCompileError("expect '('");
            }
        } else if (this.lookhead.getType() == Token.TokenType.Variable) {//没有grouping,只能有一个分组字段

            codeGenerator.onConstant(lookhead);
            move(true);
        } else {
            reportCompileError("illegal token");
        }
    }

    private void parseLogicalOr() {
        parseLogicalAnd();
        while (true) {
            Token<?> opToken = this.lookhead;
            if (expectChar('|')) {
                codeGenerator.onJoinLeft(opToken);
                move(true);
                if (expectChar('|')) {
                    move(true);
                    parseLogicalAnd();
                    codeGenerator.onJoinRight(opToken);
                } else {
                    reportCompileError("expect '|'");
                }
            } else {
                break;
            }
        }
    }

    private void parseLogicalAnd() {
        parseComparison();
        while (true) {
            Token<?> opToken = this.lookhead;
            if (expectChar('&')) {
                codeGenerator.onAndLeft(opToken);
                move(true);
                if (expectChar('&')) {
                    move(true);
                    parseComparison();
                    codeGenerator.onAndRight(opToken);
                } else {
                    reportCompileError("expect '&'");
                }
            } else {
                break;
            }
        }
    }

    private void parseComparison() {
        parseAdditionSubtraction();
        while (true) {
            Token<?> opToken = this.lookhead;
            if (expectChar('<')) {
                move(true);
                if (expectChar('=')) {
                    move(true);
                    parseAdditionSubtraction();
                    codeGenerator.onLe(opToken);
                } else {
                    parseAdditionSubtraction();
                    codeGenerator.onLt(opToken);
                }
            } else if (expectChar('>')) {
                move(true);
                if (expectChar('=')) {
                    move(true);
                    parseAdditionSubtraction();
                    codeGenerator.onGe(opToken);
                } else {
                    parseAdditionSubtraction();
                    codeGenerator.onGt(opToken);
                }
            } else if (expectChar('=')) {
                move(true);
                if (expectChar('=')) { //支持'='和'==' 相同
                    move(true);
                }
                parseAdditionSubtraction();
                codeGenerator.onEq(opToken);
            } else if (expectChar('!')) {
                move(true);
                if (expectChar('=')) {
                    move(true);
                    parseAdditionSubtraction();
                    codeGenerator.onNeq(opToken);
                } else {
                    reportCompileError("expect '='");
                }
            } else {
                break;
            }
        }
    }

    private void parseMultiplicationDivision() {
        parseUnary();
        while (true) {
            Token<?> opToken = this.lookhead;
            if (expectChar('*')) {
                move(true);
                parseUnary();
                codeGenerator.onMult(opToken);
            } else if (expectChar('/')) {
                move(true);
                parseUnary();
                codeGenerator.onDiv(opToken);
            } else if (expectChar('%')) {
                move(true);
                parseUnary();
                codeGenerator.onMod(opToken);
            } else {
                break;
            }
        }
    }

    private void parseAdditionSubtraction() {
        parseMultiplicationDivision();
        while (true) {
            Token<?> opToken = this.lookhead;
            if (expectChar('+')) {
                move(true);
                parseMultiplicationDivision();
                codeGenerator.onAdd(opToken);
            } else if (expectChar('-')) {
                move(true);
                parseMultiplicationDivision();
                codeGenerator.onSub(opToken);
            } else if (expectString("union")) {
                move(true);
                parseMultiplicationDivision();
                codeGenerator.onUnion(opToken);
            } else {
                break;
            }
        }
    }

    private void parseUnary() {
        Token<?> opToken = this.lookhead;
        if (expectChar('!')) {
            move(true);
            parseUnary();
            codeGenerator.onNot(opToken);
        } else if (expectChar('-')) {
            move(true);
            parseUnary();
            codeGenerator.onNeg(opToken);
        } else {
            parseFactor();
        }
    }

    private void parseFactor() {
        if (this.lookhead == null) {
            reportCompileError("illegal token");
        }
        Token<?> tmpPrev = getPrevToken();
        if (expectChar('[') && tmpPrev == null) {
            newList();
            return;
        }

        if (expectChar('{')) {
            int startIndex = this.lexer.getCurrentIndex();
            boolean hasDefaultOperation = false;
            int index = 0;
            while (true) {
                this.lookhead = this.lexer.scan();
                if (this.lookhead == null) {
                    reportCompileError("MetricUnit expression is incomplete");
                }
                if (expectChar('}')) {
                    index++;
                    String metricUnitString = lexer.getExpression().substring(startIndex, this.lexer.getCurrentIndex() - 1);
                    MetricUnitExpression metricUnitExpression = (MetricUnitExpression) compiler.compileMetricUnit(metricUnitString, index);
                    VariableToken variableToken = new VariableToken(metricUnitExpression.getPlaceHolder(), lookhead.getLineNo(), lookhead.getStartIndex());
                    variableToken.withMeta("IS_METRIC", true);
                    codeGenerator.onConstant(variableToken);
                    if (hasDefaultOperation) {
                        codeGenerator.onDefaultOperation(this.lookhead);
                    }
                    move(true);
                    return;
                } else if (expectChar(';')) {
                    index++;
                    String metricUnitString = lexer.getExpression().substring(startIndex, this.lexer.getCurrentIndex() - 1);
                    MetricUnitExpression metricUnitExpression = (MetricUnitExpression) compiler.compileMetricUnit(metricUnitString, index);
                    VariableToken variableToken = new VariableToken(metricUnitExpression.getPlaceHolder(), lookhead.getLineNo(), lookhead.getStartIndex());
                    variableToken.withMeta("IS_METRIC", true);
                    codeGenerator.onConstant(variableToken);
                    if (hasDefaultOperation) {
                        codeGenerator.onDefaultOperation(this.lookhead);
                    }
                    hasDefaultOperation = true;
                }
            }
        }

        if (expectChar('(')) {
            move(true);
            parseEntry();
            if (expectChar(')')) {
                move(true);
            } else {
                reportCompileError("expected ')'");
            }
        } else if (this.lookhead.getType() == Token.TokenType.Number
                || this.lookhead.getType() == Token.TokenType.String
                || this.lookhead.getType() == Token.TokenType.Variable
        ) {

            move(true);
            Token<?> prev = getPrevToken();
            if (prev.getType() == Token.TokenType.Variable && expectChar('(')) {
                method(prev);
            } else if (prev.getType() == Token.TokenType.Variable && this.lookhead != null && "in".equalsIgnoreCase(this.lookhead.getLexeme())) {
                move(true);
                if (expectChar('[')) {
                    codeGenerator.onConstant(prev);
                    newList();
                    codeGenerator.onIn(this.lookhead);
                } else {
                    codeGenerator.onConstant(prev);
                    parseEntry();
                    codeGenerator.onIn(this.lookhead);
                }
            } else {
                codeGenerator.onConstant(prev);
            }
        } else {
            reportCompileError("invalid token");
        }
    }


    private void newList() {
        codeGenerator.onMethodName(new VariableToken("[]", 0, -1));
        move(true);
        int index = 0;
        parseEntry();
        index++;
        codeGenerator.onMethodParameter(this.lookhead);
        while (expectChar(',')) {
            move(true);
            parseEntry();
            codeGenerator.onMethodParameter(this.lookhead);
            index++;
        }
        if (expectChar(']')) {
            codeGenerator.onMethodInvoke(lookhead);
            move(true);
        } else {
            reportCompileError("expected ']'");
        }
    }

    private void method(final Token<?> methodName) {
        if (instance.isAggFunction(methodName.getLexeme())) {
            AggFunctionText functionText = aggFunction(methodName);
            compiler.compileAggFunctionUnit(functionText);
            VariableToken variableToken = new VariableToken(functionText.getPlaceHolder(), methodName.getLineNo(), methodName.getStartIndex());
            variableToken.withMeta("IS_METRIC", true);
            codeGenerator.onConstant(variableToken);
            this.lookhead = this.lexer.scan();
            return;
        }
        codeGenerator.onMethodName(methodName);
        move(true);
        if (!expectChar(')')) {

            parseEntry();
            codeGenerator.onMethodParameter(this.lookhead);

            while (expectChar(',')) {
                move(true);

                parseEntry();
                codeGenerator.onMethodParameter(this.lookhead);
            }
        }

        if (expectChar(')')) {
            codeGenerator.onMethodInvoke(this.lookhead);
            move(true);
        } else {
            reportCompileError(methodName + " expected ')'");
        }
    }

    private AggFunctionText aggFunction(Token<?> methodName) {
        String name = methodName.getLexeme();
        AggFunctionText aggFunctionText = new AggFunctionText(name);
        int parenCount = 1;
        int startIndex = this.lexer.getCurrentIndex();
        while (true) {
            this.lookhead = this.lexer.scan();
            if (this.lookhead == null) {
                reportCompileError(" AggFunction [" + name + "] expression is incomplete, and it expect ')' ");
            }
            if (expectChar('(')) {
                parenCount++;
            } else if (expectChar(')')) {
                parenCount--;
                if (parenCount == 0) {
                    break;
                }
            }
        }
        aggFunctionText.setInnerString(lexer.getExpression().substring(startIndex, this.lexer.getCurrentIndex() - 1));
//        aggFunctionTexts.addAggFunctionText(aggFunctionText);
        return aggFunctionText;
    }

    protected boolean expectChar(final char ch) {
        if (this.lookhead == null) {
            return false;
        }
        return this.lookhead.getType() == Token.TokenType.Char && ((CharToken) this.lookhead).getCh() == ch;
    }

    private boolean expectString(final String op) {
        if (this.lookhead == null) {
            return false;
        }
        return this.lookhead.getType() == Token.TokenType.Variable && op.equalsIgnoreCase(this.lookhead.getLexeme());
    }


    public void move(final boolean analyse) {
        if (this.lookhead != null) {
            this.prevTokens.push(this.lookhead);
            this.lookhead = this.lexer.scan();
        } else {
            reportCompileError("illegal token");
        }
    }

    public Token<?> getPrevToken() {
        return this.prevTokens.peek();
    }

    public void reportCompileError(final String message) {

        int index = this.lexer.getCurrentIndex();
        if (this.lookhead != null) {
            index = this.lookhead.getStartIndex();
        }

        String errorMessage = String.format("Error:%s , compile [%s] , at %d, token %s", this.lexer.getExpression(), message, index, this.lookhead);

        throw new ExpressionCompileException(errorMessage);
    }

    @Override
    public SymbolTable getSymbolTable() {
        return null;
    }

    @Override
    public CodeGenerator getCodeGenerator() {
        return this.codeGenerator;
    }

    @Override
    public SoloscanLexer getLexer() {
        return this.lexer;
    }
}
