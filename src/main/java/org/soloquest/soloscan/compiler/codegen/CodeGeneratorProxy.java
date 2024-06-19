package org.soloquest.soloscan.compiler.codegen;

import org.soloquest.soloscan.SoloscanExecutor;
import org.soloquest.soloscan.compiler.parser.Parser;

public class CodeGeneratorProxy<T> extends AbstractCodeGenerator<T> {
    private final AbstractRealCodeGenerator<T> codeGen;

    private final SoloscanExecutor instance;
    private final SoloscanClassloader classLoader;
    private Parser parser;


    public CodeGeneratorProxy(final SoloscanExecutor instance,
                              final SoloscanClassloader classLoader, final AbstractRealCodeGenerator codeGen) {
        this.instance = instance;
        this.classLoader = classLoader;
        this.codeGen = codeGen;
        if (codeGen instanceof AggInnerRealCodeGenerator) {
            this.codeGen.setTokens(merticsTokenContainer, groupingTokenContainer, filterTokenContainer, filterTokenContainer);
        } else {
            this.codeGen.setTokens(merticsTokenContainer, groupingTokenContainer, filterTokenContainer, merticsTokenContainer);
        }


    }

    @Override
    public void setParser(final Parser parser) {
        this.parser = parser;
        this.codeGen.setParser(parser);
    }


    @Override
    public T getResult() {
        return (T) this.codeGen.getResult();
    }

}
