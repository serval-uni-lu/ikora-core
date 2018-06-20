package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.Step;

import java.io.LineNumberReader;
import java.io.IOException;

public class StepParser {
    public static Line parse(LineNumberReader reader, String[] tokens, Step step) throws IOException {
        tokens = ParsingUtils.removeIndent(tokens);
        String first = tokens[0];

        Line line;

        if(first.equalsIgnoreCase(":FOR")) {
            line = Line.getNextLine(reader);
        }
        else {
            parseKeywordNameAndParameter(step, tokens);
            line = Line.getNextLine(reader);
        }

        return line;
    }

    static public void parseKeywordNameAndParameter(Step keyword, String[] tokens) {
        tokens = ParsingUtils.removeIndent(tokens);

        if(tokens.length > 0) {
            keyword.setName(tokens[0]);
        }

        if (tokens.length > 1) {
            for(int i = 1; i < tokens.length; ++i) {
                keyword.addParameter(tokens[i]);
            }
        }
    }
}
