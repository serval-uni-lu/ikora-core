package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.exception.InvalidNumberArgumentException;
import org.ikora.model.TimeOut;

public class TimeoutParser {
    private TimeoutParser() {}

    public static TimeOut parse(LineReader reader, Tokens tokens, ErrorManager errors) throws Exception {
        Tokens fullTokens = tokens.withoutIndent();
        Tokens currentTokens = fullTokens.withoutTag("\\[Timeout\\]");

        TimeOut timeOut = parseLine(reader, currentTokens, errors);

        if(timeOut != null){
            timeOut.setPosition(ParserUtils.getPosition(fullTokens));
        }

        return timeOut;
    }

    private static TimeOut parseLine(LineReader reader, Tokens tokens, ErrorManager errors) throws InvalidNumberArgumentException {
        if(tokens.size() == 0){
            return null;
        }

        if(tokens.size() > 1){
            throw new InvalidNumberArgumentException(1, 2);
        }

        String text = ParserUtils.getLabel(reader, tokens, errors);

        if(text.equalsIgnoreCase("NONE")){
            return null;
        }

        TimeOut timeOut = new TimeOut(text);

        if(!timeOut.isValid()){
            return null;
        }

        return timeOut;
    }
}
