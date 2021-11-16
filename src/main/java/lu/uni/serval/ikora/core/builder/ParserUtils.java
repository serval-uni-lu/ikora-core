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

import lu.uni.serval.ikora.core.exception.InvalidArgumentException;
import lu.uni.serval.ikora.core.exception.MalformedVariableException;
import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.error.ErrorMessages;
import lu.uni.serval.ikora.core.model.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class ParserUtils {
    private ParserUtils(){}

    static <K extends KeywordDefinition> Optional<K> createKeyword(Class<K> type, LineReader reader, Tokens tokens, ErrorManager errors) {
        if(tokens.size() > 1){
            errors.registerSyntaxError(
                    reader.getSource(),
                    "Keyword definition cannot take arguments",
                    Range.fromTokens(tokens.withoutFirst(), reader.getCurrent())
            );

            return Optional.empty();
        }

        if(tokens.isEmpty()){
            errors.registerInternalError(
                    reader.getSource(),
                    "Should have at least one token, but found none",
                    Range.fromTokens(tokens, reader.getCurrent())
            );

            return Optional.empty();
        }

        K keyword = null;

        try {
            final Constructor<K> constructor = type.getConstructor(Token.class);
            keyword = constructor.newInstance(tokens.first());
            keyword.addToken(tokens.first());
            setEmbeddedVariables(keyword, reader, tokens, errors);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            errors.registerInternalError(
                    reader.getSource(),
                    "Failed to generate keyword",
                    Range.fromTokens(tokens, reader.getCurrent())
            );
        }

        return Optional.ofNullable(keyword);
    }

    static Token parseHeaderName(LineReader reader, Tokens tokens, ErrorManager errors){
        Token header = tokens.first();

        if(header.isEmpty()){
            errors.registerSyntaxError(reader.getSource(),
                    "Failed to parse block header",
                    Range.fromLine(reader.getCurrent())
            );
        }
        else if(!header.isBlock()){
            errors.registerSyntaxError(reader.getSource(),
                    "Expecting block header",
                    Range.fromToken(header, reader.getCurrent())
            );
        }

        return header;
    }

    static void parseTimeOut(LineReader reader, Tokens tokens, Delayable delayable, ErrorManager errors) {
        try {
            TimeOut timeOut = TimeoutParser.parse(tokens);
            delayable.setTimeOut(timeOut);
        } catch (InvalidArgumentException | MalformedVariableException e) {
            errors.registerSyntaxError(reader.getSource(),
                    String.format("%s: %s", ErrorMessages.FAILED_TO_PARSE_TIMEOUT, e.getMessage()),
                    Range.fromTokens(tokens, reader.getCurrent())
            );
        }
    }

    static Token getLabel(LineReader reader, Tokens tokens, ErrorManager errors){
        if(tokens.isEmpty()){
            errors.registerInternalError(
                    reader.getSource(),
                    "Not expecting an empty token",
                    Range.fromTokens(tokens, reader.getCurrent())
            );

            return Token.empty();
        }

        return tokens.first();
    }

    private static <K> void setEmbeddedVariables(K keyword, LineReader reader, Tokens tokens, ErrorManager errors){
        if(!UserKeyword.class.isAssignableFrom(keyword.getClass())){
            return;
        }

        NodeList<Variable> arguments = new NodeList<>(Token.empty());

        for(Token embeddedVariable: ValueResolver.findVariables(tokens.first())){
            try {
                arguments.add(Variable.create(embeddedVariable));
            } catch (MalformedVariableException e) {
                errors.registerInternalError(
                        reader.getSource(),
                        "Failed to parse embedded argument",
                        Range.fromToken(embeddedVariable, reader.getCurrent())
                );
            }
        }

        ((UserKeyword)keyword).setParameters(arguments);
    }

    public static NodeList<Literal> parseTags(Token label, Tokens tokens) {
        NodeList<Literal> tags = new NodeList<>(label.setType(Token.Type.LABEL));

        for(Token token: tokens){
            tags.add(new Literal(token));
        }

        return tags;
    }
}
