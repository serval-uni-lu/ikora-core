/*
 *
 *     Copyright © 2019 - 2022 University of Luxembourg
 *
 *     Licensed under the Apache License, Version 2.0 (the "License")
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package lu.uni.serval.ikora.core.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    @Test
    void testCompareSameStart(){
        final Token token1 = new Token("And ", 47, 4, 8, Token.Type.KEYWORD);
        final Token token2 = new Token("And the user follows the flow to register their organisation", 47, 4, 64, Token.Type.KEYWORD);
        assertEquals(-1, token1.compareTo(token2));
    }

    @Test
    void testSorted(){
        final Token token1 = new Token("12", 18, 14, 16, Token.Type.KEYWORD);
        final Token token2 = new Token("1234567890", 18, 4, 14, Token.Type.TEXT);
        final Token token3 = new Token("345", 18, 7, 10, Token.Type.VARIABLE);
        final List<Token> tokens = new ArrayList<>(2);
        tokens.add(token1);
        tokens.add(token2);
        tokens.add(token3);

        final List<Token> sorted = tokens.stream().sorted().collect(Collectors.toList());

        assertEquals(sorted.get(0), token2);
        assertEquals(sorted.get(1), token3);
        assertEquals(sorted.get(2), token1);
    }
}
