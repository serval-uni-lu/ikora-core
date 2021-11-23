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
import java.util.Optional;

public class VariableAssignmentParser {
    private VariableAssignmentParser(){ }

    public static Optional<VariableAssignment> parse(LineReader reader, ErrorManager errors) throws IOException {
        Tokens tokens = LexerUtils.tokenize(reader);

        if(tokens.isEmpty()){
            errors.registerInternalError(
                    reader.getSource(),
                    ErrorMessages.EMPTY_TOKEN_NOT_EXPECTED,
                    Range.fromLine(reader.getCurrent())
            );

            return Optional.empty();
        }

        Optional<Variable> optional = VariableAssignmentParser.parseName(tokens.first());

        if(!optional.isPresent()){
            errors.registerSyntaxError(
                    reader.getSource(),
                    String.format("Invalid variable: %s", tokens.first().getText()),
                    Range.fromToken(tokens.first(), reader.getCurrent())
            );

            return Optional.empty();
        }

        VariableAssignment variable = new VariableAssignment(optional.get());
        VariableAssignmentParser.parseValues(variable, tokens.withoutFirst(), reader, errors);

        return Optional.of(variable);
    }

    public static Optional<Variable> parseName(final Token name) {
        Token cleanName = trimEquals(name);
        return VariableParser.parse(cleanName);
    }

    public static void parseValues(final VariableAssignment variable, Tokens values, LineReader reader, ErrorManager errors){
        if(values.isEmpty()){
            errors.registerSyntaxError(
                    reader.getSource(),
                    String.format("Empty variable definition: %s", variable.getDefinitionToken()),
                    Range.fromTokens(variable.getTokens(), reader.getCurrent())
            );
        }

        for(Token value: values){
            variable.addValue(ValueParser.parseValue(value));
        }
    }
}
