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
import lu.uni.serval.ikora.core.utils.TokenUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class AssignmentParser {
    private AssignmentParser(){}

    public static Assignment parse(LineReader reader, Token first, Iterator<Token> tokenIterator, ErrorManager errors) {
        final List<Variable> returnValues = new ArrayList<>();

        Token current = first;
        Optional<Variable> variable = VariableParser.parse(TokenUtils.trimRightEquals(current));
        Token equalSign = TokenUtils.extractEqualSign(current);
        KeywordCall expression = null;

        if(!variable.isPresent()){
            errors.registerInternalError(
                    reader.getSource(),
                    ErrorMessages.ASSIGNMENT_SHOULD_START_WITH_VARIABLE,
                    Range.fromToken(current)
            );

            return null;
        }

        returnValues.add(variable.get());

        while(tokenIterator.hasNext()){
            current = tokenIterator.next();
            variable = VariableParser.parse(TokenUtils.trimRightEquals(current));

            if(variable.isPresent() && equalSign.isEmpty()){
                returnValues.add(variable.get());
                equalSign = TokenUtils.extractEqualSign(current);
            }
            else {
                expression = KeywordCallParser.parse(reader, current, tokenIterator, false, errors);
            }
        }

        final Token name = expression != null ? expression.getDefinitionToken() : Token.empty();
        final Assignment assignment = new Assignment(name, returnValues, expression, equalSign);

        if(!assignment.getKeywordCall().isPresent()){
            errors.registerSyntaxError(
                    reader.getSource(),
                    ErrorMessages.ASSIGNMENT_SHOULD_HAVE_LEFT_HAND_OPERAND,
                    Range.fromTokens(assignment.getTokens())
            );
        }

        return assignment;
    }
}
