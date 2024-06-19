package org.soloquest.soloscan.compiler.parser;

import org.soloquest.soloscan.exception.ExpressionCompileException;

import java.util.Deque;

public class ScopeInfo {
    int parenDepth;
    int bracketDepth;
    int braceDepth;

    enum DepthState {
        Parent, Bracket, Brace,
    }

    Deque<DepthState> depthState;

    public ScopeInfo(final int parenDepth, final int bracketDepth,
                     final int braceDepth, final Deque<DepthState> depthState) {
        super();
        this.parenDepth = parenDepth;
        this.bracketDepth = bracketDepth;
        this.braceDepth = braceDepth;
        this.depthState = depthState;
    }


    void enterParen() {
        this.parenDepth++;
        this.depthState.add(DepthState.Parent);
    }

    void leaveParen() {
        this.parenDepth--;
        if (this.depthState.removeLast() != DepthState.Parent) {
            throw new ExpressionCompileException("Mismatch paren");
        }
    }

}
