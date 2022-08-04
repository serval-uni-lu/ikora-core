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

import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.error.ErrorMessages;
import lu.uni.serval.ikora.core.exception.MalformedVariableException;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.types.StringType;
import lu.uni.serval.ikora.core.utils.StringUtils;

import java.util.Iterator;

public class ArgumentsParser {
    private ArgumentsParser() {}

    public static boolean is(Token label){
        return StringUtils.matchesIgnoreCase(label, "\\[arguments\\]");
    }

    public static NodeList<Argument> parse(LineReader reader, Token label, Iterator<Token> tokenIterator, ErrorManager errors) {
        final NodeList<Argument> arguments = new NodeList<>(label);

        int position = 0;
        while (tokenIterator.hasNext()){
            Token token = tokenIterator.next();

            try {
                final Argument argument = new Argument(
                        Variable.create(token),
                        new StringType(token.getText()),
                        position++
                );

                arguments.add(argument);

            } catch (MalformedVariableException e) {
                errors.registerSyntaxError(
                        reader.getSource(),
                        String.format("%s: %s", ErrorMessages.FAILED_TO_PARSE_PARAMETER, e.getMessage()),
                        Range.fromToken(token)
                );
            }
        }

        return arguments;
    }
}
