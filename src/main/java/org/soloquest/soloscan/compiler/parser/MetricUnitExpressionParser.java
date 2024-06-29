package org.soloquest.soloscan.compiler.parser;

import org.soloquest.soloscan.BaseMetricUnitExpression;
import org.soloquest.soloscan.SoloscanExecutor;
import org.soloquest.soloscan.compiler.SoloscanCompileInterface;
import org.soloquest.soloscan.compiler.codegen.CodeGenerator;
import org.soloquest.soloscan.compiler.lexer.SoloscanLexer;
import org.soloquest.soloscan.utils.MiscUtils;

public class MetricUnitExpressionParser extends AbstractParser {
    public MetricUnitExpressionParser(SoloscanCompileInterface compiler, SoloscanExecutor instance, SoloscanLexer lexer, CodeGenerator codeGenerator) {
        super(compiler, instance, lexer, codeGenerator);
        codeGenerator.setParser(this);
    }

    public BaseMetricUnitExpression parseMetricUnitExpression() {
        parseMetricPart();
        if (expectChar(',')) {
            move(true);
            if (lookhead != null && !expectChar(',') && !expectChar(';')) {
                parseGroupingPart();
            }
        }
        if (expectChar(',')) {
            move(true);
            if (lookhead != null && !expectChar(',') && !expectChar(';')) {
                parseFitlerPart();
            }
        }
        if (!MiscUtils.isBlank(globalFilter)) {
            lexer.appendString(globalFilter);
            globalFilter = null;
            this.lookhead = this.lexer.scan();
            parseFitlerPart();
        }

        if (lookhead == null) {
            return (BaseMetricUnitExpression) codeGenerator.getResult();
        }
        move(true);
        return (BaseMetricUnitExpression) codeGenerator.getResult();
    }
}
