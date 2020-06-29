package tech.ikora.builder;

import tech.ikora.error.ErrorManager;
import tech.ikora.error.ErrorMessages;
import tech.ikora.exception.InvalidArgumentException;
import tech.ikora.exception.MalformedVariableException;
import tech.ikora.model.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ParserUtils {
    private ParserUtils(){}

    static <K extends KeywordDefinition> Optional<K> createKeyword(Class<K> type, LineReader reader, Tokens tokens, ErrorManager errors) {
        if(tokens.size() > 1){
            errors.registerSyntaxError(
                    reader.getFile(),
                    "Keyword definition cannot take arguments",
                    Range.fromTokens(tokens.withoutFirst(), reader.getCurrent())
            );

            return Optional.empty();
        }

        if(tokens.isEmpty()){
            errors.registerInternalError(
                    reader.getFile(),
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
                    reader.getFile(),
                    "Failed to generate keyword",
                    Range.fromTokens(tokens, reader.getCurrent())
            );
        }

        return Optional.ofNullable(keyword);
    }

    static Token parseHeaderName(LineReader reader, Tokens tokens, ErrorManager errors){
        Token header = tokens.first();

        if(header.isEmpty()){
            errors.registerSyntaxError(reader.getFile(),
                    "Failed to parse block header",
                    Range.fromLine(reader.getCurrent())
            );
        }
        else if(!header.isBlock()){
            errors.registerSyntaxError(reader.getFile(),
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
            errors.registerSyntaxError(reader.getFile(),
                    String.format("%s: %s", ErrorMessages.FAILED_TO_PARSE_TIMEOUT, e.getMessage()),
                    Range.fromTokens(tokens, reader.getCurrent())
            );
        }
    }

    static Token getLabel(LineReader reader, Tokens tokens, ErrorManager errors){
        if(tokens.isEmpty()){
            errors.registerInternalError(
                    reader.getFile(),
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

        ParameterList arguments = new ParameterList(Token.empty());

        for(Token embeddedVariable: ValueResolver.findVariables(tokens.first())){
            try {
                arguments.add(Variable.create(embeddedVariable));
            } catch (MalformedVariableException e) {
                errors.registerInternalError(
                        reader.getFile(),
                        "Failed to parse embedded argument",
                        Range.fromToken(embeddedVariable, reader.getCurrent())
                );
            }
        }

        ((UserKeyword)keyword).setArgumentList(arguments);
    }
}
