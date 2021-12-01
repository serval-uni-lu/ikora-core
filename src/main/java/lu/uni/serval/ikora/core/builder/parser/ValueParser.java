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

import lu.uni.serval.ikora.core.model.*;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;

public class ValueParser {
    private ValueParser() {}

    public static Optional<DictionaryEntry> parseEntry(Token token){
        final Pair<Token, Token> keyValuePair = LexerUtils.getKeyValuePair(token);

        if(keyValuePair.getKey().isEmpty() || keyValuePair.getValue().isEmpty()){
            return Optional.empty();
        }

        final SourceNode key = parseValue(keyValuePair.getKey());
        final SourceNode value = parseValue(keyValuePair.getValue());

        return Optional.of(new DictionaryEntry(key, value));
    }

    public static Value parseValue(Token token){
        final Optional<Variable> variable = VariableParser.parse(token);
        if(variable.isPresent()){
            return variable.get();
        }

        final Optional<DictionaryEntry> entry = parseEntry(token);
        if(entry.isPresent()){
            return entry.get();
        }

        return LiteralParser.parse(token);
    }
}
