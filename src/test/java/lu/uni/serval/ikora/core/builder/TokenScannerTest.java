package lu.uni.serval.ikora.core.builder;

import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.model.Tokens;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenScannerTest {
    @Test
    void testWithoutSkipping(){
        final Tokens tokens = new Tokens(
                Token.fromString("\t"),
                Token.fromString("[Arugments]"),
                Token.fromString("\t"),
                Token.fromString("${arg1}"),
                Token.fromString("${arg2}")
        );

        int counter = 0;
        for(Token token: TokenScanner.from(tokens)) ++counter;

        assertEquals(5, counter);
    }
}