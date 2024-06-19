package org.soloquest.soloscan.compiler.lexer.token;

import java.util.Map;

public class NumberToken extends AbstractToken<Number> {

    private final Number value;


    public NumberToken(final Number value, final String lexeme, final int lineNo,
                       final int startIndex) {
        super(lexeme, lineNo, startIndex);
        this.value = value;
    }


    public Number getNumber() {
        return this.value;
    }


    @Override
    public Number getJavaValue(final Map<String, Object> env) {
        return this.value;
    }


    @Override
    public TokenType getType() {
        return TokenType.Number;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((this.value == null) ? 0 : this.value.hashCode());
        return result;
    }


    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        NumberToken other = (NumberToken) obj;
        if (this.value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!this.value.equals(other.value)) {
            return false;
        }
        return true;
    }

}
