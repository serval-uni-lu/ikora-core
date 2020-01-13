package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.error.ErrorMessages;
import org.ikora.exception.InvalidArgumentException;
import org.ikora.model.*;

import java.io.IOException;
import java.util.Optional;

public class ParserUtils {
    private ParserUtils(){}

    static void parseName(LineReader reader, Tokens tokens, KeywordDefinition keyword, ErrorManager errors) throws IOException {
        if(tokens.size() > 1){
            errors.registerSyntaxError(
                    reader.getFile(),
                    "Test definition cannot take arguments",
                    getPosition(tokens)
            );

            reader.readLine();
            return;
        }

        Optional<Token> first = tokens.first();

        if(!first.isPresent()){
            errors.registerInternalError(
                    reader.getFile(),
                    "Should have at least one token, but found none",
                    getPosition(tokens)
            );

            reader.readLine();
            return;
        }

        keyword.setName(first.get().getValue());
        reader.readLine();
    }

    static void parseTimeOut(String label, LineReader reader, Tokens tokens, Delayable delayable, ErrorManager errors) throws IOException {
        try {
            TimeOut timeOut = TimeoutParser.parse(label, tokens);
            delayable.setTimeOut(timeOut);
        } catch (InvalidArgumentException e) {
            errors.registerSyntaxError(reader.getFile(),
                    String.format("%s: %s", ErrorMessages.FAILED_TO_PARSE_TIMEOUT, e.getMessage()),
                    ParserUtils.getPosition(tokens));
        }

        reader.readLine();
    }

    static String getLabel(LineReader reader, Tokens tokens, ErrorManager errors){
        Optional<Token> first = tokens.first();
        if(!first.isPresent()){
            errors.registerInternalError(
                    reader.getFile(),
                    "Not expecting an empty token",
                    getPosition(reader.getCurrent())
            );

            return "";
        }

        return first.get().getValue();
    }

    static Position getPosition(Token start, Token end) {
        if(start == null || end == null){
            return Position.createInvalid();
        }

        Mark startMark = new Mark(start.getLine(), start.getStartOffset());
        Mark endMark = new Mark(end.getLine(), end.getEndOffset());

        return new Position(startMark, endMark);
    }

    static Position getPosition(Tokens tokens) {
        Optional<Token> start = tokens.first();
        Optional<Token> end = tokens.last();

        if(start.isPresent() && end.isPresent()){
            return getPosition(start.get(), end.get());
        }

        return Position.createInvalid();
    }

    static Position getPosition(Line line){
        Mark startMark = new Mark(line.getNumber(), 0);
        Mark endMark = new Mark(line.getNumber(), line.getText().length());

        return new Position(startMark, endMark);
    }
}
