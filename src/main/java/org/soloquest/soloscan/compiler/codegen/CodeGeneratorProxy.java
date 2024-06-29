package org.soloquest.soloscan.compiler.codegen;

import org.soloquest.soloscan.compiler.parser.Parser;

public class CodeGeneratorProxy extends AbstractCodeGenerator {
    private final AbstractRealCodeGenerator codeGen;


    public CodeGeneratorProxy(final AbstractRealCodeGenerator codeGen) {
        this.codeGen = codeGen;
        this.codeGen.setTokens(merticsTokenContainer, groupingTokenContainer, filterTokenContainer);
    }

    @Override
    public void setParser(final Parser parser) {
        this.codeGen.setParser(parser);
    }


    @Override
    public Object getResult() {
        return this.codeGen.getResult();
    }

}
