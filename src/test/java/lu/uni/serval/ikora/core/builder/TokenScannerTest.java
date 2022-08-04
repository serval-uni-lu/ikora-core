package lu.uni.serval.ikora.core.builder;

/*-
 * #%L
 * Ikora Core
 * %%
 * Copyright (C) 2019 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import lu.uni.serval.ikora.core.parser.TokenScanner;
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
