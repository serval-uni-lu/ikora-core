package lu.uni.serval.ikora.builder;

import lu.uni.serval.ikora.exception.InvalidArgumentException;
import lu.uni.serval.ikora.exception.InvalidNumberArgumentException;
import lu.uni.serval.ikora.exception.MalformedVariableException;
import lu.uni.serval.ikora.model.TimeOut;
import lu.uni.serval.ikora.model.Token;
import lu.uni.serval.ikora.model.Tokens;

public class TimeoutParser {
    private TimeoutParser() {}

    public static TimeOut parse(Tokens tokens) throws InvalidArgumentException, MalformedVariableException {
        Tokens fullTokens = tokens.withoutIndent();
        return parseLine(fullTokens);
    }

    private static TimeOut parseLine(Tokens tokens) throws InvalidArgumentException, MalformedVariableException {
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
