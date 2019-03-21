package org.ukwikora.compiler;

import org.ukwikora.model.LineRange;
import org.ukwikora.model.Step;
import org.ukwikora.model.UserKeyword;

import java.io.IOException;

class UserKeywordParser {

    public static UserKeyword parse(LineReader reader, DynamicImports dynamicImports) throws IOException {
        UserKeyword userKeyword = new UserKeyword();
        int startLine = reader.getCurrent().getNumber();

        Line test = reader.getCurrent();
        String[] tokens = LexerUtils.tokenize(test.getText());
        userKeyword.setName(tokens[0].trim());

        reader.readLine();

        while(reader.getCurrent().isValid()) {
            if(reader.getCurrent().ignore()) {
                reader.readLine();
                continue;
            }

            if(!LexerUtils.isInBlock(test.getText(), reader.getCurrent().getText())){
                break;
            }

            tokens = LexerUtils.removeIndent(LexerUtils.tokenize(reader.getCurrent().getText()));

            String label = tokens[0].trim();

            if (LexerUtils.compareNoCase(label, "\\[documentation\\]")) {
                parseDocumentation(reader, tokens, userKeyword);
            }
            else if (LexerUtils.compareNoCase(label, "\\[tags\\]")) {
                parseTags(reader, tokens, userKeyword);
            }
            else if (LexerUtils.compareNoCase(label, "\\[arguments\\]")) {
                parseParameters(reader, tokens, userKeyword);
            }
            else if (LexerUtils.compareNoCase(label, "\\[return\\]")) {
                parseReturn(reader, tokens, userKeyword);
            }
            else if (LexerUtils.compareNoCase(label, "\\[teardown\\]")) {
                parseTeardown(reader, tokens, userKeyword);
            }
            else if (LexerUtils.compareNoCase(label, "\\[timeout\\]")) {
                 parseTimeout(reader, tokens, userKeyword);
            }
            else {
                parseStep(reader, userKeyword, dynamicImports);
            }
        }

        int endLine = reader.getCurrent().getNumber();
        userKeyword.setLineRange(new LineRange(startLine, endLine));

        return userKeyword;
    }

    private static void parseDocumentation(LineReader reader, String[] tokens, UserKeyword userKeyword) throws IOException {
        StringBuilder builder = new StringBuilder();
         LexerUtils.parseDocumentation(reader, builder);

        userKeyword.setDocumentation(builder.toString());
    }

    private static void parseTags(LineReader reader, String[] tokens, UserKeyword userKeyword) throws IOException {
        tokens = LexerUtils.removeIndent(tokens);

        for(int i = 1; i < tokens.length; ++i){
            userKeyword.addTag(tokens[i]);
        }

        reader.readLine();
    }

    private static void parseParameters(LineReader reader, String[] tokens, UserKeyword userKeyword) throws IOException {
        tokens = LexerUtils.removeIndent(tokens);

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

    private static void parseStep(LineReader reader, UserKeyword userKeyword, DynamicImports dynamicImports) throws IOException {
        Step step = StepParser.parse(reader);
        userKeyword.addStep(step);

        dynamicImports.add(userKeyword, step);
    }

}
