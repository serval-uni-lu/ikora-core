package lu.uni.serval.ikora.core.parser;

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

import lu.uni.serval.ikora.core.builder.DynamicImports;
import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.model.Tokens;
import lu.uni.serval.ikora.core.model.UserKeyword;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class UserKeywordParserTest {
    @Test
    void testParseSimpleKeyword() throws IOException {
        String text = "Keyword with cool name\n\n" +
                "    Step one\tArg1\n" +
                "    Step two without args\n" +
                "    Step three \"Embedded arg\" \n";

        ErrorManager errors = new ErrorManager();
        UserKeyword keyword = createKeyword(text, errors);
        assertNotNull(keyword);
        assertTrue(errors.isEmpty());

        assertEquals("Keyword with cool name", keyword.getName());
        assertEquals(3, keyword.getSteps().size());
        assertEquals("Step one", keyword.getStep(0).getName());
        assertEquals("Step two without args", keyword.getStep(1).getName());
        assertEquals("Step three \"Embedded arg\"", keyword.getStep(2).getName());
        assertEquals(5, keyword.getTokens().size());
    }

    @Test
    void testParseDocumentation() throws IOException {
        String text = "Keyword with documentation\n" +
                "    [Documentation]\tSimple documentation\n" +
                "    Step one\tArg1\n" +
                "    Step two without args\n" +
                "    Step three \"Embedded arg\" \n";

        ErrorManager errors = new ErrorManager();
        UserKeyword keyword = createKeyword(text, errors);
        assertNotNull(keyword);
        assertTrue(errors.isEmpty());

        assertEquals("Keyword with documentation", keyword.getDefinitionToken().toString());
        assertEquals(3, keyword.getSteps().size());
        assertEquals("Simple documentation", keyword.getDocumentation().toString());
        assertEquals(7, keyword.getTokens().size());
    }

    @Test
    void testParseTags() throws IOException {
        String text = "Keyword with tags\n" +
                "   [Tags]\ttag1\ttag2 with space\ttag3\n" +
                "   Step1\n";

        ErrorManager errors = new ErrorManager();
        UserKeyword keyword = createKeyword(text, errors);
        assertNotNull(keyword);
        assertTrue(errors.isEmpty());

        assertEquals("Keyword with tags", keyword.getDefinitionToken().toString());
        assertEquals(3, keyword.getTags().size());
        assertEquals(1, keyword.getTags().stream().filter(t -> t.getName().equals("tag1")).count());
        assertEquals(1, keyword.getTags().stream().filter(t -> t.getName().equals("tag2 with space")).count());
        assertEquals(1, keyword.getTags().stream().filter(t -> t.getName().equals("tag3")).count());
        assertEquals(6, keyword.getTokens().size());
    }

    @Test
    void testDocumentationParseSameAsName() throws IOException {
        String text = "Keyword doing something\n" +
                "\t[Documentation]\tKeyword doing something\n" +
                "\t Some step";

        ErrorManager errors = new ErrorManager();
        UserKeyword keyword = createKeyword(text, errors);
        assertNotNull(keyword);
        assertTrue(errors.isEmpty());

        assertEquals(keyword.getDefinitionToken().toString(), keyword.getDocumentation().toString());
    }

    private UserKeyword createKeyword(String text, ErrorManager errors) throws IOException {
        DynamicImports dynamicImports = new DynamicImports();
        LineReader reader = new LineReader(text);
        reader.readLine();

        Tokens tokens = LexerUtils.tokenize(reader);

        return UserKeywordParser.parse(reader, tokens, dynamicImports, errors);
    }
}
