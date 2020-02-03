package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.error.ErrorMessages;
import org.ikora.exception.InvalidTypeException;
import org.ikora.exception.MalformedVariableException;
import org.ikora.model.*;
import org.ikora.utils.StringUtils;

import java.io.IOException;
import java.util.List;

class UserKeywordParser {
    private UserKeywordParser(){}

    public static UserKeyword parse(LineReader reader, DynamicImports dynamicImports, ErrorManager errors) throws IOException {
        UserKeyword userKeyword = new UserKeyword();
        Tokens nameTokens = LexerUtils.tokenize(reader.getCurrent());
        Tokens tokens;

        final List<Variable> embeddedVariables = ParserUtils.parseName(reader, nameTokens.withoutIndent(), userKeyword, errors);

        for(Variable embeddedVariable: embeddedVariables){
            userKeyword.addEmbeddedVariable(embeddedVariable);
            userKeyword.addToken(embeddedVariable.getName());
        }

        while(reader.getCurrent().isValid()) {
            if(reader.getCurrent().ignore()) {
                reader.readLine();
                continue;
            }

            Tokens currentTokens = LexerUtils.tokenize(reader.getCurrent());

            if(!nameTokens.isParent(currentTokens)){
                break;
            }

            tokens = currentTokens.withoutIndent();

            Token label = ParserUtils.getLabel(reader, tokens, errors);

            if (StringUtils.compareNoCase(label, "\\[documentation\\]")) {
                userKeyword.addToken(label.setType(Token.Type.LABEL));
                parseDocumentation(reader, tokens.withoutFirst(), userKeyword);
            }
            else if (StringUtils.compareNoCase(label, "\\[tags\\]")) {
                userKeyword.addToken(label.setType(Token.Type.LABEL));
                parseTags(reader, tokens.withoutFirst(), userKeyword);
            }
            else if (StringUtils.compareNoCase(label, "\\[arguments\\]")) {
                userKeyword.addToken(label.setType(Token.Type.LABEL));
                parseParameters(reader, tokens.withoutFirst(), userKeyword, errors);
            }
            else if (StringUtils.compareNoCase(label, "\\[return\\]")) {
                userKeyword.addToken(label.setType(Token.Type.LABEL));
                parseReturn(reader, tokens.withoutFirst(), userKeyword, errors);
            }
            else if (StringUtils.compareNoCase(label, "\\[teardown\\]")) {
                userKeyword.addToken(label.setType(Token.Type.LABEL));
                parseTeardown(reader, tokens.withoutFirst(), userKeyword, errors);
            }
            else if (StringUtils.compareNoCase(label, "\\[timeout\\]")) {
                userKeyword.addToken(label.setType(Token.Type.LABEL));
                ParserUtils.parseTimeOut(reader, tokens.withoutFirst(), userKeyword, errors);
            }
            else {
                parseStep(reader, currentTokens, userKeyword, dynamicImports, errors);
            }
        }

        return userKeyword;
    }

    private static void parseDocumentation(LineReader reader, Tokens tokens, UserKeyword userKeyword) throws IOException {
        StringBuilder builder = new StringBuilder();
        userKeyword.addTokens(LexerUtils.parseMultiLine(reader, tokens, builder));
        userKeyword.setDocumentation(builder.toString());
    }

    private static void parseTags(LineReader reader, Tokens tokens, UserKeyword userKeyword) throws IOException {
        userKeyword.addTokens(tokens);

        for(Token token: tokens){
            userKeyword.addTag(token.getText());
            userKeyword.addToken(token.setType(Token.Type.TAG));
        }

        reader.readLine();
    }

    private static void parseParameters(LineReader reader, Tokens tokens, UserKeyword userKeyword, ErrorManager errors) throws IOException {
        for(Token token: tokens){
            try {
                userKeyword.addParameter(Variable.create(token));
                userKeyword.addToken(token.setType(Token.Type.VARIABLE));
            } catch (MalformedVariableException e) {
                errors.registerSyntaxError(
                        reader.getFile(),
                        String.format("%s: %s", ErrorMessages.FAILED_TO_PARSE_PARAMETER, e.getMessage()),
                        Position.fromToken(token)
                );
            }
        }

        reader.readLine();
    }

    private static void parseReturn(LineReader reader, Tokens tokens, UserKeyword userKeyword, ErrorManager errors) throws IOException {
        for(Token token: tokens){
            try {
                userKeyword.addReturnVariable(Variable.create(token));
                userKeyword.addToken(token.setType(Token.Type.VARIABLE));
            } catch (MalformedVariableException e) {
                errors.registerSyntaxError(
                        reader.getFile(),
                        String.format("%s: %s", ErrorMessages.RETURN_VALUE_SHOULD_BE_A_VARIABLE, e.getMessage()),
                        Position.fromToken(token)
                );
            }
        }

        reader.readLine();
    }

    private static void parseTeardown(LineReader reader, Tokens tokens, UserKeyword userKeyword, ErrorManager errors) throws IOException {
        Step step = StepParser.parse(reader, tokens, false, errors);

        try {
            userKeyword.setTearDown(step);
            userKeyword.addTokens(step.getTokens());
        } catch (InvalidTypeException e) {
            errors.registerSyntaxError(
                    step.getFile(),
                    String.format("%s: %s", ErrorMessages.FAILED_TO_PARSE_TEARDOWN, e.getMessage()),
                    step.getPosition()
            );
        }

        reader.readLine();
    }

    private static void parseStep(LineReader reader, Tokens tokens, UserKeyword userKeyword, DynamicImports dynamicImports, ErrorManager errors) throws IOException {
        Step step = StepParser.parse(reader, tokens, false, errors);

        try {
            userKeyword.addStep(step);
            dynamicImports.add(userKeyword, step);
            userKeyword.addTokens(step.getTokens());
        } catch (Exception e) {
           errors.registerSyntaxError(
                   step.getSourceFile().getFile(),
                   ErrorMessages.FAILED_TO_ADD_STEP_TO_KEYWORD,
                   step.getPosition()
            );
        }
    }

}
