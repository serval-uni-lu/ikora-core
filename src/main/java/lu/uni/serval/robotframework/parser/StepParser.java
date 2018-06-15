package lu.uni.serval.robotframework.parser;

import lu.uni.serval.robotframework.model.Step;

public class StepParser {
    public static Step parse(String[] tokens) {
        Step step = new Step();

        tokens = ParsingUtils.removeIndent(tokens);
        String first = tokens[0];

        if(first.equalsIgnoreCase(":FOR")) {

        }
        else {
            ParsingUtils.parseKeywordNameAndArguments(step, tokens);
        }

        return step;
    }
}
