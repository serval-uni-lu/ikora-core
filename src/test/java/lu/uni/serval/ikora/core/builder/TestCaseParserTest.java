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

import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.model.TestCase;
import lu.uni.serval.ikora.core.model.Tokens;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TestCaseParserTest {
    @Test
    void testTestCaseParsingWithDocumentation() throws IOException {
        String text = "Test case with documentation\n" +
                "\t[Documentation]\tSome documentation\n" +
                "...\twith continuation\n\n" +
                "\tSome keyword";

        ErrorManager errors = new ErrorManager();
        TestCase testCase = createTestCase(text, errors);

        assertNotNull(testCase);
        assertEquals("Test case with documentation", testCase.getName());
        assertEquals(1, testCase.getSteps().size());
        assertEquals("Some documentation with continuation", testCase.getDocumentation().toString());
        assertEquals("Some keyword", testCase.getStep(0).getName());
    }

    @Test
    void testTestCaseParsingContainingForLoop() throws IOException {
        String text = "Test case with control flow elements\n" +
                    "    First Keyword from project B\n" +
                    "    :FOR    ${INDEX}    IN RANGE    1    3\n" +
                    "    \\    Log    ${INDEX}\n" +
                    "    Run Keyword If  ${True}  Log  ${conditional}\n";

        ErrorManager errors = new ErrorManager();
        TestCase testCase = createTestCase(text, errors);

        assertNotNull(testCase);
        assertEquals("Test case with control flow elements", testCase.getName());
        assertEquals(3, testCase.getSteps().size());
        assertEquals("First Keyword from project B", testCase.getStep(0).getName());
        assertEquals(":FOR", testCase.getStep(1).getName());
        assertEquals("Run Keyword If", testCase.getStep(2).getName());
    }

    private TestCase createTestCase(String text, ErrorManager errors) throws IOException {
        DynamicImports dynamicImports = new DynamicImports();
        LineReader reader = new LineReader(text);
        reader.readLine();

        Tokens tokens = LexerUtils.tokenize(reader);

        return TestCaseParser.parse(reader, tokens, dynamicImports, errors);
    }
}
