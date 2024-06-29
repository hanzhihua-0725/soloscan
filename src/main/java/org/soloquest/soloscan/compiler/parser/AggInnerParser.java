package org.soloquest.soloscan.compiler.parser;

import org.soloquest.soloscan.SoloscanExecutor;
import org.soloquest.soloscan.compiler.SoloscanCompileInterface;
import org.soloquest.soloscan.compiler.codegen.CodeGenerator;
import org.soloquest.soloscan.compiler.lexer.SoloscanLexer;
import org.soloquest.soloscan.runtime.aggfunction.AggInner;

public class AggInnerParser extends AbstractParser {

    public AggInnerParser(SoloscanCompileInterface compiler, SoloscanExecutor instance, SoloscanLexer lexer, CodeGenerator codeGenerator) {
        super(compiler, instance, lexer, codeGenerator);
        codeGenerator.setParser(this);
    }

    public AggInner parseAggFunctionInner() {
        parseFitlerPart();
        if (this.lookhead != null) {
            reportCompileError("parserFilteWithOneArg unexpected " + this.lookhead);
        }
        return (AggInner) codeGenerator.getResult();
    }

    public AggInner parseXAggFunctionInner() {
        parseMetricPart();
        if (expectChar(',')) {
            move(true);
            parseFitlerPart();
        } else {
            reportCompileError("parseFilterWithTwoArg expect ','");
        }
        if (this.lookhead != null) {
            reportCompileError("twoArgTernary unexpected " + this.lookhead);
        }
        return (AggInner) codeGenerator.getResult();
    }
}
