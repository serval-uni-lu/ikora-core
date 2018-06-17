package lu.uni.serval.robotframework.parser;

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

        while(line.isValid()) {
            if(line.isEmpty()) {
                line = Line.getNextLine(reader);
                continue;
            }

            tokens = line.tokenize();

            if(!tokens[0].isEmpty()){
                break;
            }

            if(tokens.length < 2) {
                continue;
            }

            String label = tokens[1];

            if (ParsingUtils.compareNoCase(label, "\\[documentation\\]")) {
                line = parseDocumentation(reader, tokens, userKeyword);
            }
            else if (ParsingUtils.compareNoCase(label, "\\[tags\\]")) {
                line = parseTags(reader, tokens, userKeyword);
            }
            else if (ParsingUtils.compareNoCase(label, "\\[arguments\\]")) {
                line = parseArguments(reader, tokens, userKeyword);
            }
            else if (ParsingUtils.compareNoCase(label, "\\[return\\]")) {
                line = parseReturn(reader, tokens, userKeyword);
            }
            else if (ParsingUtils.compareNoCase(label, "\\[teardown\\]")) {
                line = parseTeardown(reader, tokens, userKeyword);
            }
            else if (ParsingUtils.compareNoCase(label, "\\[timeout\\]")) {
                line = parseTimeout(reader, tokens, userKeyword);
            }
            else {
                line = parseStep(reader, tokens, userKeyword);
            }
        }

        keywordTable.add(userKeyword);

        return line;
    }

    private static Line parseDocumentation(LineNumberReader reader, String[] tokens, UserKeyword userKeyword) throws IOException {
        StringBuilder builder = new StringBuilder();
         Line line = ParsingUtils.parseDocumentation(reader, tokens, builder);

        userKeyword.setDocumentation(builder.toString());

        return line;
    }

    private static Line parseTags(LineNumberReader reader, String[] tokens, UserKeyword userKeyword) throws IOException {
        tokens = ParsingUtils.removeIndent(tokens);

        for(int i = 1; i < tokens.length; ++i){
            userKeyword.addTag(tokens[i]);
        }

        return Line.getNextLine(reader);
    }

    private static Line parseArguments(LineNumberReader reader, String[] tokens, UserKeyword userKeyword) throws IOException {
        tokens = ParsingUtils.removeIndent(tokens);

        for(int i = 1; i < tokens.length; ++i){
            userKeyword.addArgument(tokens[i]);
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

    private static Line parseStep(LineNumberReader reader, String[] tokens, UserKeyword userKeyword) throws IOException {
        Step step = new Step();
        Line line = StepParser.parse(reader, tokens, step);
        userKeyword.addStep(step);

        return line;
    }

}
