package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.ForLoop;
import lu.uni.serval.robotframework.model.Step;

import java.io.LineNumberReader;
import java.io.IOException;

public class StepParser {
    public static Line parse(LineNumberReader reader, Line line, Step step) throws IOException {
        String[] tokens = line.tokenize();
        tokens = Utils.removeIndent(tokens);

        Line newLine;
        if(tokens[0].equalsIgnoreCase(":FOR")) {
            newLine = parseForLoop(reader, line, step);
        }
        else if (Utils.compareNoCase(tokens[0], "^(\\$\\{)(.*)(\\})(\\s?)(=?)")){
            newLine = parseAssignment(reader, line, step);
        }
        else {
            newLine = parseKeywordNameAndParameter(reader, line, step);
        }

        return newLine;
    }

    private static Line parseForLoop(LineNumberReader reader, Line line, Step step) throws IOException {
        StringBuilder builder = new StringBuilder(line.toString());
        Line newLine = Utils.appendMultiline(reader, builder);

        String forLoop = builder.toString();

        while (newLine.isInBlock(line)){
            newLine = Line.getNextLine(reader);
        }

        return newLine;
    }

    private static Line parseAssignment(LineNumberReader reader, Line line, Step step) throws IOException {
        return Line.getNextLine(reader);
    }

    static private Line parseKeywordNameAndParameter(LineNumberReader reader, Line line, Step keyword) throws IOException {
        String[] tokens = line.tokenize();
        tokens = Utils.removeIndent(tokens);

        if(tokens.length > 0) {
            keyword.setName(tokens[0]);
        }

        if (tokens.length > 1) {
            for(int i = 1; i < tokens.length; ++i) {
                keyword.addParameter(tokens[i]);
            }
        }

        return Line.getNextLine(reader);
    }
}
