package tech.ikora.builder;

import tech.ikora.error.ErrorManager;
import tech.ikora.error.ErrorMessages;
import tech.ikora.exception.InvalidArgumentException;
import tech.ikora.exception.InvalidDependencyException;
import tech.ikora.exception.MalformedVariableException;
import tech.ikora.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParserUtils {
    private ParserUtils(){}

    static List<Variable> parseKeywordName(LineReader reader, Tokens tokens, KeywordDefinition keyword, ErrorManager errors) throws IOException {
        if(tokens.size() > 1){
            errors.registerSyntaxError(
                    reader.getFile(),
                    "Keyword definition cannot take arguments",
                    Position.fromTokens(tokens.withoutFirst(), reader.getCurrent())
            );

            return Collections.emptyList();
        }

        if(tokens.isEmpty()){
            errors.registerInternalError(
                    reader.getFile(),
                    "Should have at least one token, but found none",
                    Position.fromTokens(tokens, reader.getCurrent())
            );

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
                        Position.fromToken(embeddedArgument, reader.getCurrent())
                );
            }
        }

        return embeddedArguments;
    }

    static Token parseHeaderName(LineReader reader, Tokens tokens, ErrorManager errors){
        Token header = tokens.first();

        if(header.isEmpty()){
            errors.registerSyntaxError(reader.getFile(),
                    "Failed to parse block header",
                    Position.fromLine(reader.getCurrent())
            );
        }
        else if(!header.isBlock()){
            errors.registerSyntaxError(reader.getFile(),
                    "Expecting block header",
                    Position.fromToken(header, reader.getCurrent())
            );
        }

        return header;
    }

    static void parseTimeOut(LineReader reader, Tokens tokens, Delayable delayable, ErrorManager errors) throws IOException {
        try {
            TimeOut timeOut = TimeoutParser.parse(tokens);
            delayable.setTimeOut(timeOut);
        } catch (InvalidArgumentException | MalformedVariableException | InvalidDependencyException e) {
            errors.registerSyntaxError(reader.getFile(),
                    String.format("%s: %s", ErrorMessages.FAILED_TO_PARSE_TIMEOUT, e.getMessage()),
                    Position.fromTokens(tokens, reader.getCurrent())
            );
        }
    }

    static Token getLabel(LineReader reader, Tokens tokens, ErrorManager errors){
        if(tokens.isEmpty()){
            errors.registerInternalError(
                    reader.getFile(),
                    "Not expecting an empty token",
                    Position.fromTokens(tokens, reader.getCurrent())
            );

            return Token.empty();
        }

        return tokens.first();
    }
}
