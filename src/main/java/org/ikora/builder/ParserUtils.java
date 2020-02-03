package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.error.ErrorMessages;
import org.ikora.exception.InvalidArgumentException;
import org.ikora.exception.InvalidDependencyException;
import org.ikora.exception.MalformedVariableException;
import org.ikora.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParserUtils {
    private ParserUtils(){}

    static List<Variable> parseName(LineReader reader, Tokens tokens, KeywordDefinition keyword, ErrorManager errors) throws IOException {
        if(tokens.size() > 1){
            errors.registerSyntaxError(
                    reader.getFile(),
                    "Keyword definition cannot take arguments",
                    Position.fromTokens(tokens.withoutFirst())
            );

            reader.readLine();
            return Collections.emptyList();
        }

        if(tokens.isEmpty()){
            errors.registerInternalError(
                    reader.getFile(),
                    "Should have at least one token, but found none",
                    Position.fromTokens(tokens)
            );

            reader.readLine();
            return Collections.emptyList();
        }

        keyword.setName(tokens.first());
        keyword.addToken(tokens.first());

        List<Variable> embeddedArguments = new ArrayList<>();

        for(Token embeddedArgument: ValueLinker.findVariables(tokens.first())){
            try {
                embeddedArguments.add(Variable.create(embeddedArgument));
            } catch (MalformedVariableException e) {
                errors.registerInternalError(
                        reader.getFile(),
                        "Failed to parse embedded argument",
                        Position.fromToken(embeddedArgument)
                );
            }
        }

        reader.readLine();

        return embeddedArguments;
    }

    static void parseTimeOut(LineReader reader, Tokens tokens, Delayable delayable, ErrorManager errors) throws IOException {
        try {
            TimeOut timeOut = TimeoutParser.parse(tokens);
            delayable.setTimeOut(timeOut);
        } catch (InvalidArgumentException | MalformedVariableException | InvalidDependencyException e) {
            errors.registerSyntaxError(reader.getFile(),
                    String.format("%s: %s", ErrorMessages.FAILED_TO_PARSE_TIMEOUT, e.getMessage()),
                    Position.fromTokens(tokens));
        }

        reader.readLine();
    }

    static Token getLabel(LineReader reader, Tokens tokens, ErrorManager errors){
        if(tokens.isEmpty()){
            errors.registerInternalError(
                    reader.getFile(),
                    "Not expecting an empty token",
                    Position.fromTokens(tokens)
            );

            return Token.empty();
        }

        return tokens.first();
    }
}
