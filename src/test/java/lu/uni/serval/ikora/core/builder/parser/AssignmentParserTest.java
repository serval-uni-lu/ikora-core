package lu.uni.serval.ikora.core.builder.parser;

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
import lu.uni.serval.ikora.core.error.ErrorMessages;
import lu.uni.serval.ikora.core.error.Errors;
import lu.uni.serval.ikora.core.model.*;
import org.junit.jupiter.api.Assertions;
import lu.uni.serval.ikora.core.Helpers;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AssignmentParserTest {
    @Test
    void parseAssignmentFromKeywordToSingleVariable() throws IOException {
        String text = "\t${value}=\tKeyword returning a value";

        ErrorManager errors = new ErrorManager();
        Assignment assignment = createAssignment(text, errors);

        List<Variable> variables = assignment.getLeftHandOperand();
        assertEquals(1, variables.size());
        assertEquals("${value}", variables.get(0).getName());

        Optional<KeywordCall> expression = assignment.getKeywordCall();
        assertTrue(expression.isPresent());
        assertEquals("Keyword returning a value", expression.get().getName());
    }

    @Test
    void parseAssignmentFromKeywordToMultipleVariable() throws IOException {
        String text = "\t${value1}\t${value2}=\tKeyword returning a value";

        ErrorManager errors = new ErrorManager();
        Assignment assignment = createAssignment(text, errors);

        List<Variable> variables = assignment.getLeftHandOperand();
        assertEquals(2, variables.size());
        assertEquals("${value1}", variables.get(0).getName());
        assertEquals("${value2}", variables.get(1).getName());

        Optional<KeywordCall> expression = assignment.getKeywordCall();
        assertTrue(expression.isPresent());
        assertEquals("Keyword returning a value", expression.get().getName());
    }

    @Test
    void parseAssignmentWithNothing() throws IOException {
        final String text = "\t${value}=";

        final ErrorManager errors = new ErrorManager();
        Assignment assignment = createAssignment(text, errors);

        final List<Variable> variables = assignment.getLeftHandOperand();
        assertEquals(1, variables.size());
        assertEquals("${value}", variables.get(0).getName());

        final Optional<KeywordCall> expression = assignment.getKeywordCall();
        assertFalse(expression.isPresent());

        final Errors fileError = errors.inMemory();
        assertEquals(1, fileError.getSize());
        Assertions.assertEquals(ErrorMessages.ASSIGNMENT_SHOULD_HAVE_LEFT_HAND_OPERAND,
                fileError.getSyntaxErrors().iterator().next().getMessage());
    }

    @Test
    void parseAssignmentWithArguments() throws IOException {
        final String text = "    ${jsonfile}=    Get File     /file/path/data.json    encoding=UTF-8\n";

        final ErrorManager errors = new ErrorManager();
        final Assignment assignment = createAssignment(text, errors);

        final List<Variable> variables = assignment.getLeftHandOperand();
        assertEquals(1, variables.size());
        assertEquals("${jsonfile}", variables.get(0).getName());

        Optional<KeywordCall> expression = assignment.getKeywordCall();
        assertTrue(expression.isPresent());
        assertEquals("Get File", expression.get().getName());
    }

    @Test
    void parseAssignmentWithoutEqualSign() throws IOException {
        final String text = "    ${jsonfile}    Get File     /file/path/data.json    encoding=UTF-8\n";

        final ErrorManager errors = new ErrorManager();
        final Assignment assignment = createAssignment(text, errors);

        final List<Variable> variables = assignment.getLeftHandOperand();
        assertEquals(1, variables.size());
        assertEquals("${jsonfile}", variables.get(0).getName());

        Optional<KeywordCall> expression = assignment.getKeywordCall();
        assertTrue(expression.isPresent());
        assertEquals("Get File", expression.get().getName());
    }

    @Test
    void parseAssignmentWithoutEqualSignWithMultipleValues() throws IOException {
        final String text = "    ${jsonfile1}    ${jsonfile2}    ${jsonfile3}    Get File     /file/path/data.json    encoding=UTF-8\n";

        final ErrorManager errors = new ErrorManager();
        final Assignment assignment = createAssignment(text, errors);

        final List<Variable> variables = assignment.getLeftHandOperand();
        assertEquals(3, variables.size());
        assertEquals("${jsonfile1}", variables.get(0).getName());
        assertEquals("${jsonfile2}", variables.get(1).getName());
        assertEquals("${jsonfile3}", variables.get(2).getName());

        Optional<KeywordCall> expression = assignment.getKeywordCall();
        assertTrue(expression.isPresent());
        assertEquals("Get File", expression.get().getName());
    }

    private Assignment createAssignment(String text, ErrorManager errors) throws IOException {
        final LineReader reader = Helpers.getLineReader(text);
        final Iterator<Token> tokenIterator = TokenScanner.from(LexerUtils.tokenize(reader))
                .skipIndent(true)
                .skipTypes(Token.Type.CONTINUATION)
                .iterator();


        return AssignmentParser.parse(reader, tokenIterator.next(), tokenIterator, errors);
    }
}
