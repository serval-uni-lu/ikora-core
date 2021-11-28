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
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.utils.TokenUtils;

import java.util.Iterator;
import java.util.Optional;

public class VariableAssignmentParser {
    private VariableAssignmentParser(){ }

    public static Optional<VariableAssignment> parse(LineReader reader, Iterator<Token> tokenIterator, ErrorManager errors) {
        if(!tokenIterator.hasNext()){
            errors.registerInternalError(
                    reader.getSource(),
                    ErrorMessages.EMPTY_TOKEN_NOT_EXPECTED,
                    Range.fromLine(reader.getCurrent())
            );

            return Optional.empty();
        }

        final Token variableToken = tokenIterator.next();
        final Optional<Variable> optional = VariableAssignmentParser.parseName(variableToken);

        if(!optional.isPresent()){
            errors.registerSyntaxError(
                    reader.getSource(),
                    String.format("Invalid variable: %s", variableToken.getText()),
                    Range.fromToken(variableToken)
            );

            return Optional.empty();
        }

        final Token equalSign = TokenUtils.extractEqualSign(variableToken);

        final VariableAssignment variable = new VariableAssignment(optional.get(), equalSign);
        parseValues(variable, tokenIterator, reader, errors);

        return Optional.of(variable);
    }

    public static Optional<Variable> parseName(final Token name) {
        final Token cleanName = TokenUtils.trimRightEquals(name);
        return VariableParser.parse(cleanName);
    }

    public static void parseValues(final VariableAssignment variable, Iterator<Token> tokenIterator, LineReader reader, ErrorManager errors){
        if(!tokenIterator.hasNext()){
            errors.registerSyntaxError(
                    reader.getSource(),
                    String.format("Empty variable definition: %s", variable.getDefinitionToken()),
                    Range.fromTokens(variable.getTokens())
            );
        }

        while (tokenIterator.hasNext()){
            final Token value = tokenIterator.next();
            variable.addValue(ValueParser.parseValue(value));
        }
    }
}
