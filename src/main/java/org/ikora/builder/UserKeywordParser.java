package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.model.LineRange;
import org.ikora.model.Step;
import org.ikora.model.UserKeyword;

import java.io.IOException;
import java.util.Optional;

class UserKeywordParser {

    public static UserKeyword parse(LineReader reader, DynamicImports dynamicImports, ErrorManager errors) throws IOException {
        UserKeyword userKeyword = new UserKeyword();
        int startLine = reader.getCurrent().getNumber();

        Tokens keywordTokens = LexerUtils.tokenize( reader.getCurrent().getText());

        ParserUtils.parseName(reader, keywordTokens.withoutIndent(), userKeyword, errors);

        while(reader.getCurrent().isValid()) {
            if(reader.getCurrent().ignore()) {
                reader.readLine();
                continue;
            }

            Tokens tokens = LexerUtils.tokenize(reader.getCurrent().getText());

            if(!keywordTokens.isParent(tokens)){
                break;
            }

            tokens = tokens.withoutIndent();

            String label = ParserUtils.getLabel(reader, tokens, errors);

            if (LexerUtils.compareNoCase(label, "\\[documentation\\]")) {
                parseDocumentation(reader, userKeyword);
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
                parseTeardown(reader, tokens, userKeyword, errors);
            }
            else if (LexerUtils.compareNoCase(label, "\\[timeout\\]")) {
                 parseTimeout(reader, tokens, userKeyword);
            }
            else {
                parseStep(reader, tokens, userKeyword, dynamicImports, errors);
            }
        }

        int endLine = reader.getCurrent().getNumber();
        userKeyword.setLineRange(new LineRange(startLine, endLine));

        return userKeyword;
    }

    private static void parseDocumentation(LineReader reader, UserKeyword userKeyword) throws IOException {
        StringBuilder builder = new StringBuilder();
         LexerUtils.parseDocumentation(reader, builder);

        userKeyword.setDocumentation(builder.toString());
    }

    private static void parseTags(LineReader reader, Tokens tokens, UserKeyword userKeyword) throws IOException {
        for(Token token: tokens){
            userKeyword.addTag(token.getValue());
        }

        reader.readLine();
    }

    private static void parseParameters(LineReader reader, Tokens tokens, UserKeyword userKeyword) throws IOException {
        for(Token token: tokens){
            userKeyword.addParameter(token.getValue());
        }

        reader.readLine();
    }

    private static void parseReturn(LineReader reader, Tokens tokens, UserKeyword userKeyword) throws IOException {
        for(Token token: tokens.withoutTag("\\[return\\]")){
            userKeyword.addReturn(token.getValue());
        }

        reader.readLine();
    }

    private static void parseTeardown(LineReader reader, Tokens tokens, UserKeyword userKeyword, ErrorManager errors) throws IOException {
        Step step = StepParser.parse(reader, tokens, "\\[teardown\\]", errors);
        userKeyword.setTearDown(step);

        reader.readLine();
    }

    private static void parseTimeout(LineReader reader, Tokens tokens, UserKeyword userKeyword) throws IOException {
        reader.readLine();
    }

    private static void parseStep(LineReader reader, Tokens tokens, UserKeyword userKeyword, DynamicImports dynamicImports,ErrorManager errors) throws IOException {
        Step step = StepParser.parse(reader, tokens, errors);

        try {
            userKeyword.addStep(step);
            dynamicImports.add(userKeyword, step);
        } catch (Exception e) {
           errors.registerSyntaxError("Failed to add step to user keyword",
                    step.getFile().getFile(),
                    step.getLineRange()
            );
        }
    }

}
