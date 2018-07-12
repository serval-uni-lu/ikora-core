package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.Assignment;
import lu.uni.serval.robotframework.model.ForLoop;
import lu.uni.serval.robotframework.model.KeywordCall;
import lu.uni.serval.robotframework.model.Step;

import java.io.IOException;

public class StepParser {
    public static Step parse(LineReader reader) throws IOException {
        Step step;

        String[] tokens = reader.getCurrent().tokenize();
        tokens = Utils.removeIndent(tokens);

        if(tokens[0].equalsIgnoreCase(":FOR")) {
            step = parseForLoop(reader);
        }
        else if (Utils.compareNoCase(tokens[0], "^(\\$\\{)(.*)(\\})(\\s?)(=?)")){
            step = parseAssignment(reader);
        }
        else {
            step = parseKeywordNameAndParameter(reader);
        }

        return step;
    }

    private static Step parseForLoop(LineReader reader) throws IOException {
        ForLoop forLoop = new ForLoop();

        Line loop = reader.getCurrent();

        StringBuilder builder = new StringBuilder(reader.getCurrent().toString());
        Utils.appendMultiline(reader, builder);

        forLoop.setName(builder.toString());

        while (reader.getCurrent().isInBlock(loop)){
            reader.readLine();
        }

        return forLoop;
    }

    private static Step parseAssignment(LineReader reader) throws IOException {
        Assignment assignment = new Assignment();
        assignment.setName(reader.getCurrent().getText());

        reader.readLine();

        return assignment;
    }

    static private Step parseKeywordNameAndParameter(LineReader reader) throws IOException {
        KeywordCall call = new KeywordCall();

        String[] tokens = reader.getCurrent().tokenize();
        tokens = Utils.removeIndent(tokens);

        if(tokens.length > 0) {
            call.setName(tokens[0]);
        }

        if (tokens.length > 1) {
            for(int i = 1; i < tokens.length; ++i) {
                call.addParameter(tokens[i]);
            }
        }

        reader.readLine();

        return call;
    }
}
