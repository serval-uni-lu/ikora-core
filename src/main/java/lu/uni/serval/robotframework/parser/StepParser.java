package lu.uni.serval.robotframework.parser;

import lu.uni.serval.robotframework.model.Step;

import java.io.BufferedReader;
import java.io.IOException;

public class StepParser {
    public static String parse(BufferedReader bufferedReader, String[] tokens, Step step) throws IOException {
        tokens = ParsingUtils.removeIndent(tokens);
        String first = tokens[0];

        String line = null;

        if(first.equalsIgnoreCase(":FOR")) {
            line = bufferedReader.readLine();
        }
        else {
            parseKeywordNameAndArguments(step, tokens);
            line = bufferedReader.readLine();
        }

        return line;
    }

    static public void parseKeywordNameAndArguments(Step keyword, String[] tokens) {
        tokens = ParsingUtils.removeIndent(tokens);

        if(tokens.length > 0) {
            keyword.setName(tokens[0]);
        }

        if (tokens.length > 1) {
            for(int i = 1; i < tokens.length; ++i) {
                keyword.addArgument(tokens[i]);
            }
        }
    }
}
