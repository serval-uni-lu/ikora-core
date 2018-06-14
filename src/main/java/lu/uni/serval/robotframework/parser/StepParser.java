package lu.uni.serval.robotframework.parser;

import lu.uni.serval.robotframework.model.Step;

import java.util.Arrays;

public class StepParser {
    public static Step parse(String[] tokens) {
        if(tokens[0].equals("")){
            tokens = Arrays.copyOfRange(tokens, 1, tokens.length);
        }

        Step step = new Step();

        return step;
    }
}
