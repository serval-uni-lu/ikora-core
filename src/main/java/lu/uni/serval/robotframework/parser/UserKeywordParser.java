package lu.uni.serval.robotframework.parser;

import lu.uni.serval.robotframework.model.KeywordTable;
import lu.uni.serval.robotframework.model.Step;
import lu.uni.serval.robotframework.model.UserKeyword;

import java.io.BufferedReader;
import java.io.IOException;

public class UserKeywordParser {

    public static String parse(BufferedReader bufferedReader, String test, KeywordTable keywordTable) throws IOException {
        String[] tokens = ParsingUtils.tokenizeLine(test);

        UserKeyword userKeyword = new UserKeyword();
        userKeyword.setName(tokens[0]);

        String line = bufferedReader.readLine();

        while(line != null) {
            if(line.equals("")) {
                line = bufferedReader.readLine();
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
                line = parseDocumentation(bufferedReader, tokens, userKeyword);
            }
            else if (ParsingUtils.compareNoCase(label, "\\[tags\\]")) {
                line = parseTags(bufferedReader, tokens, userKeyword);
            }
            else if (ParsingUtils.compareNoCase(label, "\\[arguments\\]")) {
                line = parseArguments(bufferedReader, tokens, userKeyword);
            }
            else if (ParsingUtils.compareNoCase(label, "\\[return\\]")) {
                line = parseReturn(bufferedReader, tokens, userKeyword);
            }
            else if (ParsingUtils.compareNoCase(label, "\\[teardown\\]")) {
                line = parseTeardown(bufferedReader, tokens, userKeyword);
            }
            else if (ParsingUtils.compareNoCase(label, "\\[timeout\\]")) {
                line = parseTimeout(bufferedReader, tokens, userKeyword);
            }
            else {
                line = parseStep(bufferedReader, tokens, userKeyword);
            }
        }

        keywordTable.add(userKeyword);

        return line;
    }

    private static String parseDocumentation(BufferedReader bufferedReader, String[] tokens, UserKeyword userKeyword) throws IOException {
        StringBuilder builder = new StringBuilder();
        String line = ParsingUtils.parseDocumentation(bufferedReader, tokens, builder);

        userKeyword.setDocumentation(builder.toString());

        return line;
    }

    private static String parseTags(BufferedReader bufferedReader, String[] tokens, UserKeyword userKeyword) throws IOException {
        tokens = ParsingUtils.removeIndent(tokens);

        for(int i = 1; i < tokens.length; ++i){
            userKeyword.addTag(tokens[i]);
        }

        return bufferedReader.readLine();
    }

    private static String parseArguments(BufferedReader bufferedReader, String[] tokens, UserKeyword userKeyword) throws IOException {
        tokens = ParsingUtils.removeIndent(tokens);

        for(int i = 1; i < tokens.length; ++i){
            userKeyword.addArgument(tokens[i]);
        }

        return bufferedReader.readLine();
    }

    private static String parseReturn(BufferedReader bufferedReader, String[] tokens, UserKeyword userKeyword) throws IOException {
        return bufferedReader.readLine();
    }

    private static String parseTeardown(BufferedReader bufferedReader, String[] tokens, UserKeyword userKeyword) throws IOException {
        return bufferedReader.readLine();
    }

    private static String parseTimeout(BufferedReader bufferedReader, String[] tokens, UserKeyword userKeyword) throws IOException {
        return bufferedReader.readLine();
    }

    private static String parseStep(BufferedReader bufferedReader, String[] tokens, UserKeyword userKeyword) throws IOException {
        Step step = new Step();
        String line = StepParser.parse(bufferedReader, tokens, step);
        userKeyword.addStep(step);

        return line;
    }

}
