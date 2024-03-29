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
package lu.uni.serval.ikora.core.parser;

import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.error.ErrorMessages;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.utils.StringUtils;

import java.io.IOException;
import java.util.Iterator;

public class TestProcessingParser {
    private TestProcessingParser() {}

    public static boolean is(Token label, Scope scope, TestProcessing.Phase phase){
        String scopeString = switch (scope) {
            case TEST -> "test ";
            case SUITE -> "suite ";
            default -> "";
        };

        String phaseString = switch (phase) {
            case SETUP -> "setup";
            case TEARDOWN -> "teardown";
            case TEMPLATE -> "template";
            default -> "";
        };

        if(phaseString.isEmpty()){
            return false;
        }

        String expression = scopeString + phaseString;
        expression = scope == Scope.KEYWORD ? "\\[" + expression + "\\]" : expression;

        return StringUtils.matchesIgnoreCase(label, expression);
    }

    public static TestProcessing parse(TestProcessing.Phase phase, LineReader reader, Token label, Iterator<Token> tokenIterator, ErrorManager errors) {
        KeywordCall call;

        if(tokenIterator.hasNext()){
            try {
                final Step step = StepParser.parse(0, reader, tokenIterator.next(), tokenIterator, false, errors);

                if(step instanceof KeywordCall keywordCall){
                    call = keywordCall;
                }
                else {
                    errors.registerSyntaxError(
                            reader.getSource(),
                            ErrorMessages.EXPECTED_KEYWORD_CALL,
                            step.getRange()
                    );

                    call = null;
                }

            } catch (IOException e) {
                errors.registerInternalError(
                        reader.getSource(),
                        ErrorMessages.FAILED_TO_PARSE_STEP,
                        Range.fromToken(label)
                );

                call = null;
            }
        }
        else{
            errors.registerSyntaxError(
                    reader.getSource(),
                    ErrorMessages.EXPECTED_KEYWORD_CALL,
                    Range.fromToken(label)
            );

            call = null;
        }

        return new TestProcessing(phase, label, call);
    }
}
