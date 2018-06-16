package lu.uni.serval.robotframework.parser;

import lu.uni.serval.robotframework.model.KeywordTable;
import lu.uni.serval.robotframework.model.Step;
import lu.uni.serval.robotframework.model.UserKeyword;

import java.io.LineNumberReader;
import java.io.IOException;

public class UserKeywordParser {

    public static String parse(LineNumberReader reader, String test, KeywordTable keywordTable) throws IOException {
        String[] tokens = ParsingUtils.tokenizeLine(test);

        UserKeyword userKeyword = new UserKeyword();
        userKeyword.setName(tokens[0]);

        String line = reader.readLine();

        while(line != null) {
            if(line.equals("")) {
                line = reader.readLine();
                continue;
            }

            tokens = ParsingUtils.tokenizeLine(line);

            if(!tokens[0].equals("")){
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

    private static String parseDocumentation(LineNumberReader reader, String[] tokens, UserKeyword userKeyword) throws IOException {
        StringBuilder builder = new StringBuilder();
        String line = ParsingUtils.parseDocumentation(reader, tokens, builder);

        userKeyword.setDocumentation(builder.toString());

        return line;
    }

    private static String parseTags(LineNumberReader reader, String[] tokens, UserKeyword userKeyword) throws IOException {
        tokens = ParsingUtils.removeIndent(tokens);

        for(int i = 1; i < tokens.length; ++i){
            userKeyword.addTag(tokens[i]);
        }

        return reader.readLine();
    }

    private static String parseArguments(LineNumberReader reader, String[] tokens, UserKeyword userKeyword) throws IOException {
        tokens = ParsingUtils.removeIndent(tokens);

        for(int i = 1; i < tokens.length; ++i){
            userKeyword.addArgument(tokens[i]);
        }

        return reader.readLine();
    }

    private static String parseReturn(LineNumberReader reader, String[] tokens, UserKeyword userKeyword) throws IOException {
        return reader.readLine();
    }

    private static String parseTeardown(LineNumberReader reader, String[] tokens, UserKeyword userKeyword) throws IOException {
        return reader.readLine();
    }

    private static String parseTimeout(LineNumberReader reader, String[] tokens, UserKeyword userKeyword) throws IOException {
        return reader.readLine();
    }

    private static String parseStep(LineNumberReader reader, String[] tokens, UserKeyword userKeyword) throws IOException {
        Step step = new Step();
        String line = StepParser.parse(reader, tokens, step);
        userKeyword.addStep(step);

        return line;
    }

}
