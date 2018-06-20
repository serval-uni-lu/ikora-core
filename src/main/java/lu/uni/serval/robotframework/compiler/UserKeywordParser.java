package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.KeywordTable;
import lu.uni.serval.robotframework.model.Step;
import lu.uni.serval.robotframework.model.UserKeyword;

import java.io.LineNumberReader;
import java.io.IOException;

public class UserKeywordParser {

    public static Line parse(LineNumberReader reader, Line test, KeywordTable keywordTable) throws IOException {
        String[] tokens = test.tokenize();

        UserKeyword userKeyword = new UserKeyword();
        userKeyword.setName(tokens[0]);

        Line line = Line.getNextLine(reader);

        while(line.isValid() && line.isInBlock(test)) {
            if(line.isEmpty()) {
                line = Line.getNextLine(reader);
                continue;
            }

            tokens = line.tokenize();

            if(tokens.length < 2) {
                continue;
            }

            String label = tokens[1];

            if (Utils.compareNoCase(label, "\\[documentation\\]")) {
                line = parseDocumentation(reader, tokens, userKeyword);
            }
            else if (Utils.compareNoCase(label, "\\[tags\\]")) {
                line = parseTags(reader, tokens, userKeyword);
            }
            else if (Utils.compareNoCase(label, "\\[arguments\\]")) {
                line = parseParameters(reader, tokens, userKeyword);
            }
            else if (Utils.compareNoCase(label, "\\[return\\]")) {
                line = parseReturn(reader, tokens, userKeyword);
            }
            else if (Utils.compareNoCase(label, "\\[teardown\\]")) {
                line = parseTeardown(reader, tokens, userKeyword);
            }
            else if (Utils.compareNoCase(label, "\\[timeout\\]")) {
                line = parseTimeout(reader, tokens, userKeyword);
            }
            else {
                line = parseStep(reader, line, userKeyword);
            }
        }

        keywordTable.add(userKeyword);

        return line;
    }

    private static Line parseDocumentation(LineNumberReader reader, String[] tokens, UserKeyword userKeyword) throws IOException {
        StringBuilder builder = new StringBuilder();
         Line line = Utils.parseDocumentation(reader, tokens, builder);

        userKeyword.setDocumentation(builder.toString());

        return line;
    }

    private static Line parseTags(LineNumberReader reader, String[] tokens, UserKeyword userKeyword) throws IOException {
        tokens = Utils.removeIndent(tokens);

        for(int i = 1; i < tokens.length; ++i){
            userKeyword.addTag(tokens[i]);
        }

        return Line.getNextLine(reader);
    }

    private static Line parseParameters(LineNumberReader reader, String[] tokens, UserKeyword userKeyword) throws IOException {
        tokens = Utils.removeIndent(tokens);

        for(int i = 1; i < tokens.length; ++i){
            userKeyword.addParameter(tokens[i]);
        }

        return Line.getNextLine(reader);
    }

    private static Line parseReturn(LineNumberReader reader, String[] tokens, UserKeyword userKeyword) throws IOException {
        return Line.getNextLine(reader);
    }

    private static Line parseTeardown(LineNumberReader reader, String[] tokens, UserKeyword userKeyword) throws IOException {
        return Line.getNextLine(reader);
    }

    private static Line parseTimeout(LineNumberReader reader, String[] tokens, UserKeyword userKeyword) throws IOException {
        return Line.getNextLine(reader);
    }

    private static Line parseStep(LineNumberReader reader, Line line, UserKeyword userKeyword) throws IOException {
        Step step = new Step();
        line = StepParser.parse(reader, line, step);
        userKeyword.addStep(step);

        return line;
    }

}
