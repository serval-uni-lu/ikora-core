package org.ukwikora.compiler;

import org.ukwikora.exception.InvalidNumberArgumentException;
import org.ukwikora.model.LineRange;
import org.ukwikora.model.TimeOut;

public class TimeoutParser {
    public static TimeOut parse(LineReader reader, String[] tokens) throws Exception {
        int startLine = reader.getCurrent().getNumber();
        int endLine = reader.readLine().getNumber();

        tokens = LexerUtils.removeIndent(tokens);
        tokens = LexerUtils.removeTag(tokens, "\\[Timeout\\]");

        TimeOut timeOut = parseLine(tokens);

        if(timeOut != null){
            timeOut.setLineRange(new LineRange(startLine, endLine));
        }

        return timeOut;
    }

    private static TimeOut parseLine(String[] tokens) throws InvalidNumberArgumentException {
        if(tokens.length == 0){
            return null;
        }

        if(tokens.length > 1){
            throw new InvalidNumberArgumentException(1, 2);
        }

        String text = tokens[0];

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
