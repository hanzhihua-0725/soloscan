package org.soloquest.soloscan.compiler.parser;

import org.soloquest.soloscan.compiler.codegen.CodeGenerator;
import org.soloquest.soloscan.compiler.lexer.SoloscanLexer;
import org.soloquest.soloscan.compiler.lexer.SymbolTable;

public interface Parser {

    SymbolTable getSymbolTable();

    CodeGenerator getCodeGenerator();

    SoloscanLexer getLexer();


}
