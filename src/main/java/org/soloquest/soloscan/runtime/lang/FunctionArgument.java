package org.soloquest.soloscan.runtime.lang;

public class FunctionArgument {
    private final int index;
    private final String expression;

    public FunctionArgument(final int index, final String name) {
        super();
        this.index = index;
        this.expression = name;
    }

    public int getIndex() {
        return this.index;
    }

    public String getExpression() {
        return this.expression;
    }

    @Override
    public String toString() {
        return "FunctionArgument [index=" + this.index + ", expression=" + this.expression + "]";
    }

    public static FunctionArgument from(final int index, final String name) {
        return new FunctionArgument(index, name);
    }
}
