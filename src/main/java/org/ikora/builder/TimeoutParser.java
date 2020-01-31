package org.ikora.builder;

import org.ikora.exception.InvalidArgumentException;
import org.ikora.exception.InvalidDependencyException;
import org.ikora.exception.InvalidNumberArgumentException;
import org.ikora.exception.MalformedVariableException;
import org.ikora.model.TimeOut;
import org.ikora.model.Token;

import java.util.Optional;

public class TimeoutParser {
    private TimeoutParser() {}

    public static TimeOut parse(Tokens tokens) throws InvalidArgumentException, MalformedVariableException, InvalidDependencyException {
        Tokens fullTokens = tokens.withoutIndent();
        return parseLine(fullTokens);
    }

    private static TimeOut parseLine(Tokens tokens) throws InvalidArgumentException, MalformedVariableException, InvalidDependencyException {
        if(tokens.size() > 3){
            throw new InvalidNumberArgumentException(2, 3);
        }

        Token name = parseName(tokens);
        Token errorMessage = parseErrorMessage(tokens);

        TimeOut timeOut = new TimeOut(name, errorMessage);

        if(!timeOut.isValid()){
            throw new InvalidArgumentException(String.format("Invalid argument: %s", name));
        }

        return timeOut;
    }

    private static Token parseName(Tokens tokens) {
        return tokens.first();
    }

    private static Token parseErrorMessage(Tokens tokens) {
       if(tokens.size() > 2){
           return tokens.get(1);
       }

       return Token.empty();
    }
}
