package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.exception.InvalidArgumentException;
import org.ikora.exception.InvalidNumberArgumentException;
import org.ikora.model.TimeOut;

public class TimeoutParser {
    private TimeoutParser() {}

    public static TimeOut parse( String label, LineReader reader, Tokens tokens, ErrorManager errors) throws InvalidArgumentException {
        Tokens fullTokens = tokens.withoutIndent();
        Tokens currentTokens = fullTokens.withoutTag(label);

        TimeOut timeOut = parseLine(reader, currentTokens, errors);
        timeOut.setPosition(ParserUtils.getPosition(fullTokens));

        return timeOut;
    }

    private static TimeOut parseLine(LineReader reader, Tokens tokens, ErrorManager errors) throws InvalidArgumentException {
        if(tokens.size() == 0){
            throw new InvalidNumberArgumentException(1, 0);
        }

        if(tokens.size() > 1){
            throw new InvalidNumberArgumentException(1, 2);
        }

        String text = ParserUtils.getLabel(reader, tokens, errors);

        TimeOut timeOut = new TimeOut(text);

        if(!timeOut.isValid()){
            throw new InvalidArgumentException(String.format("Invalid argument: %s", text));
        }

        return timeOut;
    }
}
