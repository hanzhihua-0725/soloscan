package org.soloquest.soloscan.compiler.lexer;

import org.soloquest.soloscan.SoloscanOptions;
import org.soloquest.soloscan.compiler.lexer.token.*;
import org.soloquest.soloscan.exception.ExpressionCompileException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.LinkedList;

public class SoloscanLexer {
    static final char[] VALID_HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'a',
            'B', 'b', 'C', 'c', 'D', 'd', 'E', 'e', 'F', 'f'};
    static final char[] VALID_CHAR = {'=', '>', '<', '+', '-', '*', '/', '%', '!', '&', '|', '(', ')', '[', ']', ',', ';', '{', '}'};
    private static final long OVERFLOW_FLAG = Long.MAX_VALUE / 10;
    private static final long OVERFLOW_SINGLE = Long.MAX_VALUE % 10;
    private final SymbolTable symbolTable;
    private char peek;
    private CharacterIterator iterator;
    private int lineNo;
    private LinkedList<Token<?>> tokenBuffer = new LinkedList<>();
    private String expression;

    public SoloscanLexer(final String expression) {
        this.iterator = new StringCharacterIterator(expression);
        this.expression = expression;
        this.symbolTable = new SymbolTable();
        this.peek = this.iterator.current();
        this.lineNo = 1;
    }

    public static boolean isValidChar(final char ch) {
        for (char tmp : VALID_CHAR) {
            if (tmp == ch) {
                return true;
            }
        }
        return false;
    }

    public void appendString(String string) {
        int pos = this.iterator.getIndex();
        this.expression = this.expression + string;
        this.iterator = new StringCharacterIterator(this.expression);
        this.iterator.setIndex(pos);
        this.peek = this.iterator.current();

    }

    public String getExpression() {
        return this.expression;
    }

    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

    public int getLineNo() {
        return this.lineNo;
    }

    public void pushback(final Token<?> token) {
        this.tokenBuffer.push(token);
    }

    private void nextChar() {
        this.peek = this.iterator.next();
    }

    public void prevChar() {
        this.peek = this.iterator.previous();
    }

    public boolean isValidHexChar(final char ch) {
        for (char c : VALID_HEX_CHAR) {
            if (c == ch) {
                return true;
            }
        }
        return false;
    }

    public int getCurrentIndex() {
        return this.iterator.getIndex();
    }


    private void skipBlank() {
        for (; ; nextChar()) {
            if (this.peek == ' ' || this.peek == '\t' || this.peek == '\r' || this.peek == '\n'  || this.peek == 160) {
                if (this.peek == '\n') {
                    this.lineNo++;
                }
            } else {
                break;
            }
        }
    }

    private Token processHex() {
        if (Character.isDigit(this.peek) && this.peek == '0') {
            nextChar();
            if (this.peek == 'x' || this.peek == 'X') {
                nextChar();
                StringBuilder sb = new StringBuilder();
                int startIndex = this.iterator.getIndex() - 2;
                long value = 0L;
                do {
                    sb.append(this.peek);
                    value = 16 * value + Character.digit(this.peek, 16);
                    nextChar();
                } while (isValidHexChar(this.peek));
                return new NumberToken(value, sb.toString(), this.lineNo, startIndex);
            } else {
                prevChar();
            }
        }
        return null;
    }

    private Token processVariable() {
        if (Character.isJavaIdentifierStart(this.peek)) {
            int startIndex = this.iterator.getIndex();
            StringBuilder sb = new StringBuilder();
            do {
                sb.append(this.peek);
                nextChar();
            } while (Character.isJavaIdentifierPart(this.peek) || this.peek == '.');
            String lexeme = sb.toString();
            if (SoloscanOptions.getOption(SoloscanOptions.COLUMN_CASE_INSENSITIVE)) {
                lexeme = lexeme.toLowerCase();
            }
            VariableToken variableToken = new VariableToken(lexeme, this.lineNo, startIndex);
            return this.symbolTable.reserve(variableToken);
        }
        return null;
    }

    private Token processString() {
        if (this.peek == '"' || this.peek == '\'') {
            char left = this.peek;
            int startIndex = this.iterator.getIndex();
            StringBuilder sb = new StringBuilder();
            while ((this.peek = this.iterator.next()) != left) {
                if (this.peek == '\\') { // escape
                    nextChar();
                    if (this.peek == CharacterIterator.DONE) {
                        throw new ExpressionCompileException(
                                "EOF while reading string at index: " + this.iterator.getIndex() + ", expression:" + this.expression);
                    }
                    if (this.peek == left) {
                        sb.append(this.peek);
                        continue;
                    }
                    switch (this.peek) {
                        case 't':
                            this.peek = '\t';
                            break;
                        case 'r':
                            this.peek = '\r';
                            break;
                        case 'n':
                            this.peek = '\n';
                            break;
                        case '\\':
                            break;
                        case 'b':
                            this.peek = '\b';
                            break;
                        case 'f':
                            this.peek = '\f';
                            break;
                        default: {
                            throw new ExpressionCompileException(
                                    "Unsupported escape character: \\" + this.peek);
                        }

                    }
                }

                if (this.peek == CharacterIterator.DONE) {
                    throw new ExpressionCompileException(
                            "EOF while reading string at index: " + this.iterator.getIndex());
                }

                sb.append(this.peek);
            }
            nextChar();
            return new StringToken(sb.toString(), this.lineNo, startIndex);
        }
        return null;
    }

    private Token processNumber() {
        if (Character.isDigit(this.peek) || this.peek == '.') {
            StringBuilder sb = new StringBuilder();
            int startIndex = this.iterator.getIndex();
            long lval = 0L;
            double dval = 0d;
            boolean hasDot = false;
            double d = 10.0;
            boolean isBigInt = false;
            boolean isBigDecimal = false;
            boolean scientificNotation = false;
            boolean negExp = false;
            boolean isOverflow = false;
            do {
                sb.append(this.peek);
                if (this.peek == '.') {
                    if (scientificNotation) {
                        throw new ExpressionCompileException(
                                "Illegal number " + sb + " at " + this.iterator.getIndex());
                    }
                    if (hasDot) {
                        throw new ExpressionCompileException(
                                "Illegal Number " + sb + " at " + this.iterator.getIndex());
                    } else {
                        hasDot = true;
                        nextChar();
                    }

                } else if (this.peek == 'N') {
                    if (hasDot) {
                        throw new ExpressionCompileException(
                                "Illegal number " + sb + " at " + this.iterator.getIndex());
                    }
                    isBigInt = true;
                    nextChar();
                    break;
                } else if (this.peek == 'M') {
                    isBigDecimal = true;
                    nextChar();
                    break;
                } else if (this.peek == 'e' || this.peek == 'E') {
                    if (scientificNotation) {
                        throw new ExpressionCompileException(
                                "Illegal number " + sb + " at " + this.iterator.getIndex());
                    }
                    scientificNotation = true;
                    nextChar();
                    if (this.peek == '-') {
                        negExp = true;
                        sb.append(this.peek);
                        nextChar();
                    }
                } else {
                    int digit = Character.digit(this.peek, 10);
                    if (scientificNotation) {
                        int n = digit;
                        nextChar();
                        while (Character.isDigit(this.peek)) {
                            sb.append(this.peek);
                            n = 10 * n + Character.digit(this.peek, 10);
                            nextChar();
                        }
                        while (n-- > 0) {
                            if (negExp) {
                                dval = dval / 10;
                            } else {
                                dval = 10 * dval;
                            }
                        }
                        hasDot = true;
                    } else if (hasDot) {
                        dval = dval + digit / d;
                        d = d * 10;
                        nextChar();
                    } else {
                        if (!isOverflow
                                && (lval > OVERFLOW_FLAG || (lval == OVERFLOW_FLAG && digit > OVERFLOW_SINGLE))) {
                            isOverflow = true;
                        }
                        lval = 10 * lval + digit;
                        dval = 10 * dval + digit;
                        nextChar();
                    }

                }

            } while (Character.isDigit(this.peek) || this.peek == '.' || this.peek == 'E'
                    || this.peek == 'e' || this.peek == 'M' || this.peek == 'N');

            Number value;
            if (isBigDecimal) {
                value = new BigDecimal(getBigNumberLexeme(sb));
            } else if (isBigInt) {
                value = new BigInteger(getBigNumberLexeme(sb));
            } else if (hasDot) {
                if (sb.length() == 1) {
                    return new CharToken('.', this.lineNo, startIndex);
                } else {
                    value = dval;
                }
            } else {
                if (isOverflow) {
                    value = new BigInteger(sb.toString());
                } else {
                    value = lval;
                }
            }
            String lexeme = sb.toString();
            if (isBigDecimal || isBigInt) {
                lexeme = lexeme.substring(0, lexeme.length() - 1);
            }
            return new NumberToken(value, lexeme, this.lineNo, startIndex);
        }
        return null;
    }

    public Token<?> scan() {
        if (this.tokenBuffer != null && !this.tokenBuffer.isEmpty()) {
            return this.tokenBuffer.pop();
        }
        if (this.peek == CharacterIterator.DONE) {
            return null;
        }
        skipBlank();

        if (this.peek == CharacterIterator.DONE) {
            return null;
        }

        Token token = null;
        token = processHex();
        if (token != null) {
            return token;
        }

        token = processNumber();
        if (token != null) {
            return token;
        }

        token = processVariable();
        if (token != null) {
            return token;
        }

        token = processString();
        if (token != null) {
            return token;
        }

        if (isValidChar(this.peek)) {
            token = new CharToken(this.peek, this.lineNo, this.iterator.getIndex());
            nextChar();
            return token;
        }
        throw new ExpressionCompileException(String.format("expression:%s,index:%s、char:'%c' is invalid", this.expression, getCurrentIndex(), this.peek));
    }

    public String getScanString() {
        return this.expression.substring(0, this.iterator.getIndex());
    }

    private String getBigNumberLexeme(final StringBuilder sb) {
        String lexeme = sb.toString();
        lexeme = lexeme.substring(0, lexeme.length() - 1);
        return lexeme;
    }


}


