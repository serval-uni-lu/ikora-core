package lu.uni.serval.ikora.core.model;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class TokenTest {
    private static final Pattern gherkinPattern =  Pattern.compile("(\\s*(Given|When|Then|And|But)\\s*)", Pattern.CASE_INSENSITIVE);

    @Test
    void testTrimPatternLeft(){
        final Token token = new Token("given this word", 0, 0, 14, Token.Type.TEXT);
        assertEquals("this word", token.trim(gherkinPattern).getText());
    }

    @Test
    void testTrimPatternMiddle(){
        final Token token = new Token("First Given this word", 0, 0, 14, Token.Type.TEXT);
        assertEquals("First Given this word", token.trim(gherkinPattern).getText());
    }

    @Test
    void testTrimPatternEnd(){
        final Token token = new Token("This word is a given", 0, 0, 14, Token.Type.TEXT);
        assertEquals("This word is a", token.trim(gherkinPattern).getText());
    }

    @Test
    void testTrimPatternOnly(){
        final Token token = new Token("given", 0, 0, 5, Token.Type.TEXT);
        assertTrue(token.trim(gherkinPattern).isEmpty());
    }
}