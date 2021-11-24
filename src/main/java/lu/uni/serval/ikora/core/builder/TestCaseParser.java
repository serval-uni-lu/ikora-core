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
import lu.uni.serval.ikora.core.error.ErrorMessages;
import lu.uni.serval.ikora.core.model.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;

class TestCaseParser {
    private TestCaseParser() {}

    public static TestCase parse(LineReader reader, Tokens nameTokens, DynamicImports dynamicImports, ErrorManager errors) throws IOException {
        final Iterator<Token> tokenNameIterator = TokenScanner.from(nameTokens)
                .skipIndent(true)
                .iterator();

        final Optional<TestCase> optionalTestCase = ParserUtils.createKeyword(TestCase.class, reader, tokenNameIterator, errors);

        if(!optionalTestCase.isPresent()){
            throw new IOException(String.format("failed to read test case at line %d in file %s",
                    reader.getCurrent().getNumber(),
                    reader.getSource().getAbsolutePath()));
        }

        final TestCase testCase = optionalTestCase.get();

        while(reader.getCurrent().isValid() && !LexerUtils.isBlock(reader.getCurrent().getText())) {
            if(reader.getCurrent().ignore()) {
                reader.readLine();
                continue;
            }

            if(nameTokens.getIndentSize() + 1 != LexerUtils.peek(reader.getCurrent()).getIndentSize()){
                break;
            }

            final Tokens contentTokens = LexerUtils.tokenize(reader);
            final Iterator<Token> contentTokenIterator = TokenScanner.from(contentTokens)
                    .skipIndent(true)
                    .iterator();

            parseContentLine(contentTokens.getIndentSize(), testCase, reader, contentTokenIterator, dynamicImports, errors);
        }

        return testCase;
    }

    private static void parseContentLine(int indent, TestCase testCase, LineReader reader, Iterator<Token> tokenIterator, DynamicImports dynamicImports, ErrorManager errors) throws IOException {
        Token label = ParserUtils.getLabel(reader, tokenIterator, errors);

        if (DocumentationParser.is(label, Scope.KEYWORD)) {
            final Documentation documentation = DocumentationParser.parse(label, tokenIterator);
            testCase.setDocumentation(documentation);
        }
        else if (TagsParser.is(label, Scope.KEYWORD, TagsParser.Type.NORMAL)) {
            final NodeList<Literal> tags = TagsParser.parse(label, tokenIterator);
            testCase.setTags(tags);
        }
        else if (TestProcessingParser.is(label, Scope.KEYWORD, TestProcessing.Phase.SETUP)) {
            final TestProcessing setup = TestProcessingParser.parse(TestProcessing.Phase.SETUP, reader, label, tokenIterator, errors);
            testCase.setSetup(setup);
        }
        else if (TestProcessingParser.is(label, Scope.KEYWORD, TestProcessing.Phase.TEARDOWN)) {
            final TestProcessing teardown = TestProcessingParser.parse(TestProcessing.Phase.TEARDOWN, reader, label, tokenIterator, errors);
            testCase.setTearDown(teardown);
        }
        else if (TestProcessingParser.is(label, Scope.KEYWORD, TestProcessing.Phase.TEMPLATE)) {
            final TestProcessing template = TestProcessingParser.parse(TestProcessing.Phase.TEMPLATE, reader, label, tokenIterator, errors);
            testCase.setTemplate(template);
        }
        else if (TimeoutParser.is(label, Scope.KEYWORD)) {
            final TimeOut timeOut = TimeoutParser.parse(label, tokenIterator);
            testCase.setTimeOut(timeOut);
        }
        else {
            final Step step = StepParser.parse(indent, reader, label, tokenIterator, true, errors);

            try {
                testCase.addStep(step);
                dynamicImports.add(testCase, step);
            } catch (Exception e) {
                errors.registerSyntaxError(
                        step.getSourceFile().getSource(),
                        ErrorMessages.FAILED_TO_ADD_STEP_TO_TEST_CASE,
                        step.getRange()
                );
            }
        }
    }
}
