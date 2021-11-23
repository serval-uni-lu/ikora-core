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

class UserKeywordParser {
    private UserKeywordParser(){}

    public static UserKeyword parse(LineReader reader, Tokens nameTokens, DynamicImports dynamicImports, ErrorManager errors) throws IOException {
        final Iterator<Token> tokenNameIterator = TokenScanner.from(nameTokens)
                .skipIndent(true)
                .iterator();

        final Optional<UserKeyword> optionalUserKeyword = ParserUtils.createKeyword(UserKeyword.class, reader, tokenNameIterator, errors);

        if(!optionalUserKeyword.isPresent()){
            throw new IOException(String.format("failed to read keyword at line %d in file %s",
                    reader.getCurrent().getNumber(),
                    reader.getSource().getAbsolutePath()));
        }

        final UserKeyword userKeyword = optionalUserKeyword.get();

        while(reader.getCurrent().isValid() && LexerUtils.isSameBlock(nameTokens, reader)) {
            if(reader.getCurrent().ignore()) {
                reader.readLine();
                continue;
            }

            final Iterator<Token> contentTokenIterator = TokenScanner.from(LexerUtils.tokenize(reader))
                    .skipIndent(true)
                    .iterator();

            parseContentLine(userKeyword, reader, contentTokenIterator, dynamicImports, errors);
        }

        return userKeyword;
    }

    private static void parseContentLine(UserKeyword userKeyword, LineReader reader, Iterator<Token> tokenIterator, DynamicImports dynamicImports, ErrorManager errors) throws IOException {
        final Token label = ParserUtils.getLabel(reader, tokenIterator, errors);

        if (DocumentationParser.is(label)) {
            final Documentation documentation = DocumentationParser.parse(label, tokenIterator);
            userKeyword.setDocumentation(documentation);
        }
        else if (TagsParser.is(label, TagsParser.Type.NORMAL)) {
            final NodeList<Literal> tags = TagsParser.parse(label, tokenIterator);
            userKeyword.setTags(tags);
        }
        else if (ArgumentsParser.is(label)) {
            final NodeList<Variable> arguments = ArgumentsParser.parse(reader, label, tokenIterator, errors);
            userKeyword.setParameters(arguments);
        }
        else if (ReturnParser.is(label)) {
            final NodeList<Value> returnValues = ReturnParser.parse(label, tokenIterator);
            userKeyword.setReturnVariables(returnValues);
        }
        else if (TestProcessingParser.is(label, Scope.KEYWORD, TestProcessing.Phase.TEARDOWN)) {
            final TestProcessing teardown = TestProcessingParser.parse(TestProcessing.Phase.TEARDOWN, reader, label, tokenIterator, errors);
            userKeyword.setTearDown(teardown);
        }
        else if (TimeoutParser.is(label, Scope.KEYWORD)) {
            final TimeOut timeOut = TimeoutParser.parse(label, tokenIterator);
            userKeyword.setTimeOut(timeOut);
        }
        else {
            final Step step = StepParser.parse(reader, label, tokenIterator, false, errors);

            try {
                userKeyword.addStep(step);
                dynamicImports.add(userKeyword, step);
            } catch (Exception e) {
                errors.registerSyntaxError(
                        step.getSourceFile().getSource(),
                        ErrorMessages.FAILED_TO_ADD_STEP_TO_KEYWORD,
                        step.getRange()
                );
            }
        }
    }
}
