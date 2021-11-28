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

import java.util.Iterator;
import java.util.Optional;

public class KeywordCallParser {
    private KeywordCallParser() {}

    public static KeywordCall parse(LineReader reader, Token first, Iterator<Token> tokenIterator, boolean allowGherkin, ErrorManager errors) {
        if(first == null || first.isEmpty()){
            errors.registerInternalError(
                    reader.getSource(),
                    ErrorMessages.EMPTY_TOKEN_SHOULD_BE_KEYWORD,
                    Range.fromLine(reader.getCurrent())
            );

            return null;
        }

        final Token name = allowGherkin ? Gherkin.getCleanName(first) : first;
        final Gherkin gherkin = allowGherkin ? new Gherkin(first) : Gherkin.none();
        final KeywordCall call = new KeywordCall(name);
        call.setGherkin(gherkin);

        final NodeList<Argument> arguments = new NodeList<>();

        while(tokenIterator.hasNext()) {
            final Token token = tokenIterator.next();
            final Optional<Variable> variable = VariableParser.parse(token);
            final Argument argument = new Argument(variable.isPresent() ? variable.get() : new Literal(token));
            arguments.add(argument);
        }

        call.setArgumentList(arguments);

        return call;

    }
}
