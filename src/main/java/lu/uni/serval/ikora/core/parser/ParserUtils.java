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

import lu.uni.serval.ikora.core.analytics.resolver.ValueResolver;
import lu.uni.serval.ikora.core.exception.MalformedVariableException;
import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.types.StringType;
import lu.uni.serval.ikora.core.utils.TokenUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Optional;

public class ParserUtils {
    private ParserUtils(){}

    static <K extends KeywordDefinition> Optional<K> createKeyword(Class<K> type, LineReader reader, Iterator<Token> tokenIterator, ErrorManager errors) {
        if(!tokenIterator.hasNext()){
            errors.registerInternalError(
                    reader.getSource(),
                    "Should have at least one token, but found none",
                    Range.fromLine(reader.getCurrent())
            );

            return Optional.empty();
        }

        K keyword = null;
        Token token = tokenIterator.next();

        try {
            final Constructor<K> constructor = type.getConstructor(Token.class);
            keyword = constructor.newInstance(token);
            keyword.addToken(token);
            setEmbeddedVariables(keyword, reader, token, errors);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            errors.registerInternalError(
                    reader.getSource(),
                    "Failed to generate keyword",
                    Range.fromToken(token)
            );
        }

        if(tokenIterator.hasNext()){
            errors.registerSyntaxError(
                    reader.getSource(),
                    "Keyword definition cannot take arguments",
                    Range.fromTokens(TokenUtils.accumulate(tokenIterator))
            );

            return Optional.empty();
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
                    Range.fromToken(header)
            );
        }

        return header;
    }

    static Token getLabel(LineReader reader, Iterator<Token> tokenIterator, ErrorManager errors){
        if(!tokenIterator.hasNext()){
            errors.registerInternalError(
                    reader.getSource(),
                    "Not expecting an empty token",
                    Range.fromLine(reader.getCurrent())
            );

            return Token.empty();
        }

        return tokenIterator.next();
    }

    private static <K> void setEmbeddedVariables(K keyword, LineReader reader, Token token, ErrorManager errors){
        if(!UserKeyword.class.isAssignableFrom(keyword.getClass())){
            return;
        }

        NodeList<Argument> arguments = new NodeList<>(Token.empty());

        int position = 0;
        for(Token embeddedVariable: ValueResolver.findVariables(token)){
            try {
                final Argument argument = new Argument(
                        Variable.create(embeddedVariable),
                        new StringType(token.getText()),
                        position++
                );

                arguments.add(argument);

            } catch (MalformedVariableException e) {
                errors.registerInternalError(
                        reader.getSource(),
                        "Failed to parse embedded argument",
                        Range.fromToken(embeddedVariable)
                );
            }
        }

        ((UserKeyword)keyword).setArguments(arguments);
    }
}
