package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.exception.InvalidNumberArgumentException;
import org.ikora.model.LineRange;
import org.ikora.model.TimeOut;

public class TimeoutParser {
    public static TimeOut parse(LineReader reader, Tokens tokens, ErrorManager errors) throws Exception {
        int startLine = reader.getCurrent().getNumber();
        int endLine = reader.readLine().getNumber();

        tokens = tokens.withoutIndent();
        tokens = tokens.withoutTag("\\[Timeout\\]");

        TimeOut timeOut = parseLine(reader, tokens, errors);

        if(timeOut != null){
            timeOut.setLineRange(new LineRange(startLine, endLine));
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
