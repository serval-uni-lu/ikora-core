package lu.uni.serval.ikora.core.builder;

import lu.uni.serval.ikora.core.builder.parser.TokenScanner;
import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.model.Tokens;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenScannerTest {
    private static Tokens tokens1;

    @BeforeAll
    static void setup(){
        tokens1 = new Tokens(
                Token.fromString("\t").setType(Token.Type.DELIMITER),
                Token.fromString("[Arugments]"),
                Token.fromString("\t").setType(Token.Type.DELIMITER),
                Token.fromString("${arg1}"),
                Token.fromString("...").setType(Token.Type.CONTINUATION),
                Token.fromString("${arg2}")
        );
    }

    @Test
    void testWithoutSkipping(){
        int counter = 0;
        for(Token token: TokenScanner.from(tokens1)){
            counter++;
        }

        assertEquals(6, counter);
    }

    @Test
    void testSkipIndent(){
        int counter = 0;
        for(Token token: TokenScanner.from(tokens1).skipIndent(true)){
            counter++;
        }

        assertEquals(5, counter);
    }

    @Test
    void testSkipContinuation(){
        int counter = 0;
        for(Token token: TokenScanner.from(tokens1).skipTypes(Token.Type.CONTINUATION)){
            counter++;
        }

        assertEquals(5, counter);
    }

    @Test
    void testSkipIndentAndContinuation(){
        int counter = 0;
        for(Token token: TokenScanner.from(tokens1).skipIndent(true).skipTypes(Token.Type.CONTINUATION)){
            counter++;
        }

        assertEquals(4, counter);
    }

    @Test
    void testSkipMultipleTypes(){
        int counter = 0;
        for(Token token: TokenScanner.from(tokens1).skipTypes(Token.Type.DELIMITER, Token.Type.CONTINUATION)){
            counter++;
        }

        assertEquals(3, counter);
    }
}