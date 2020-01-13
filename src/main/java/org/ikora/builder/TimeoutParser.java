package org.ikora.builder;

import org.ikora.exception.InvalidArgumentException;
import org.ikora.exception.InvalidNumberArgumentException;
import org.ikora.model.TimeOut;

import java.util.Optional;

public class TimeoutParser {
    private TimeoutParser() {}

    public static TimeOut parse(String label, Tokens tokens) throws InvalidArgumentException {
        Tokens fullTokens = tokens.withoutIndent();
        Tokens currentTokens = fullTokens.withoutTag(label);

        TimeOut timeOut = parseLine(currentTokens);
        timeOut.setPosition(ParserUtils.getPosition(fullTokens));

        return timeOut;
    }

    private static TimeOut parseLine(Tokens tokens) throws InvalidArgumentException {
        if(tokens.size() > 3){
            throw new InvalidNumberArgumentException(2, 3);
        }

        String name = parseName(tokens);
        String errorMessage = parseErrorMessage(tokens);

        TimeOut timeOut = new TimeOut(name, errorMessage);

        if(!timeOut.isValid()){
            throw new InvalidArgumentException(String.format("Invalid argument: %s", name));
        }

        return timeOut;
    }

    private static String parseName(Tokens tokens) throws InvalidArgumentException {
        Optional<Token> token = tokens.get(0);

        if(!token.isPresent()){
            throw new InvalidNumberArgumentException(1, 0);
        }

        return token.get().getValue();
    }

    private static String parseErrorMessage(Tokens tokens) {
        Optional<Token> token = tokens.get(1);

        if(!token.isPresent()){
            return "";
        }

        return token.get().getValue();
    }
}
