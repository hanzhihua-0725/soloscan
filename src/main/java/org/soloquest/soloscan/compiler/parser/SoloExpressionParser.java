package org.soloquest.soloscan.compiler.parser;

import org.soloquest.soloscan.BaseSoloExpression;
import org.soloquest.soloscan.SoloscanExecutor;
import org.soloquest.soloscan.compiler.SoloscanCompileInterface;
import org.soloquest.soloscan.compiler.codegen.CodeGenerator;
import org.soloquest.soloscan.compiler.lexer.SoloscanLexer;

public class SoloExpressionParser extends AbstractParser {

    public SoloExpressionParser(SoloscanCompileInterface compiler, SoloscanExecutor instance, SoloscanLexer lexer, CodeGenerator codeGenerator) {
        super(compiler, instance, lexer, codeGenerator);
        codeGenerator.setParser(this);
    }

    public BaseSoloExpression parseSoloExpression() {
        parseEntry();
        if (this.lookhead != null) {
            reportCompileError(this.lookhead + " was not consumed");
        }
        return (BaseSoloExpression) codeGenerator.getResult();
    }


}
