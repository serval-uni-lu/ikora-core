package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.Step;
import lu.uni.serval.robotframework.model.UserKeyword;

import java.io.IOException;

public class UserKeywordParser {

    public static UserKeyword parse(LineReader reader) throws IOException {
        UserKeyword userKeyword = new UserKeyword();

        Line test = reader.getCurrent();
        String[] tokens = test.tokenize();
        userKeyword.setName(tokens[0]);

        reader.readLine();

        while(reader.getCurrent().isValid() && reader.getCurrent().isInBlock(test)) {
            if(Utils.ignore(reader.getCurrent())) {
                reader.readLine();
                continue;
            }

            tokens = reader.getCurrent().tokenize();

            if(tokens.length < 2) {
                continue;
            }

            String label = tokens[1];

            if (Utils.compareNoCase(label, "\\[documentation\\]")) {
                parseDocumentation(reader, tokens, userKeyword);
            }
            else if (Utils.compareNoCase(label, "\\[tags\\]")) {
                parseTags(reader, tokens, userKeyword);
            }
            else if (Utils.compareNoCase(label, "\\[arguments\\]")) {
                parseParameters(reader, tokens, userKeyword);
            }
            else if (Utils.compareNoCase(label, "\\[return\\]")) {
                parseReturn(reader, tokens, userKeyword);
            }
            else if (Utils.compareNoCase(label, "\\[teardown\\]")) {
                parseTeardown(reader, tokens, userKeyword);
            }
            else if (Utils.compareNoCase(label, "\\[timeout\\]")) {
                 parseTimeout(reader, tokens, userKeyword);
            }
            else {
                parseStep(reader, userKeyword);
            }
        }

        return userKeyword;
    }

    private static void parseDocumentation(LineReader reader, String[] tokens, UserKeyword userKeyword) throws IOException {
        StringBuilder builder = new StringBuilder();
         Utils.parseDocumentation(reader, tokens, builder);

        userKeyword.setDocumentation(builder.toString());
    }

    private static void parseTags(LineReader reader, String[] tokens, UserKeyword userKeyword) throws IOException {
        tokens = Utils.removeIndent(tokens);

        for(int i = 1; i < tokens.length; ++i){
            userKeyword.addTag(tokens[i]);
        }

        reader.readLine();
    }

    private static void parseParameters(LineReader reader, String[] tokens, UserKeyword userKeyword) throws IOException {
        tokens = Utils.removeIndent(tokens);

        for(int i = 1; i < tokens.length; ++i){
            userKeyword.addParameter(tokens[i]);
        }

        reader.readLine();
    }

    private static void parseReturn(LineReader reader, String[] tokens, UserKeyword userKeyword) throws IOException {
        reader.readLine();
    }

    private static void parseTeardown(LineReader reader, String[] tokens, UserKeyword userKeyword) throws IOException {
        reader.readLine();
    }

    private static void parseTimeout(LineReader reader, String[] tokens, UserKeyword userKeyword) throws IOException {
        reader.readLine();
    }

    private static void parseStep(LineReader reader, UserKeyword userKeyword) throws IOException {
        Step step = StepParser.parse(reader);
        userKeyword.addStep(step);
    }

}
