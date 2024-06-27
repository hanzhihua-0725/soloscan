package org.soloquest.soloscan.compiler.lexer;

import org.junit.Assert;
import org.junit.Test;
import org.soloquest.soloscan.compiler.lexer.token.Token;

import java.math.BigDecimal;
import java.math.BigInteger;

public class SoloscanLexerTest {

    @Test
    public void testNumberScan() {
        SoloscanLexer lexer = new SoloscanLexer( "11+2N+3M");
        Token token = lexer.scan();
        Assert.assertEquals(11l, token.getJavaValue(null));
        token = lexer.scan();
        Assert.assertEquals('+', token.getJavaValue(null));
        token = lexer.scan();
        Assert.assertEquals(new BigInteger("2"), token.getJavaValue(null));
        token = lexer.scan();
        Assert.assertEquals('+', token.getJavaValue(null));
        token = lexer.scan();
        Assert.assertEquals(new BigDecimal("3"), token.getJavaValue(null));
    }
}
