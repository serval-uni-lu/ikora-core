package lu.uni.serval.ikora.builder;

import lu.uni.serval.ikora.exception.InvalidTypeException;
import lu.uni.serval.ikora.exception.MalformedVariableException;
import lu.uni.serval.ikora.error.ErrorManager;
import lu.uni.serval.ikora.error.ErrorMessages;
import lu.uni.serval.ikora.model.*;
import lu.uni.serval.ikora.utils.StringUtils;

import java.io.IOException;
import java.util.Optional;

class UserKeywordParser {
    private UserKeywordParser(){}

    public static UserKeyword parse(LineReader reader, Tokens nameTokens, DynamicImports dynamicImports, ErrorManager errors) throws IOException {
        Tokens tokens;

        final Optional<UserKeyword> optionalUserKeyword = ParserUtils.createKeyword(UserKeyword.class, reader, nameTokens.withoutIndent(), errors);

        if(!optionalUserKeyword.isPresent()){
            throw new IOException(String.format("failed to read keyword at line %d in file %s",
                    reader.getCurrent().getNumber(),
                    reader.getSource().getAbsolutePath()));
        }

        final UserKeyword userKeyword = optionalUserKeyword.get();

        while(reader.getCurrent().isValid()) {
            if(reader.getCurrent().ignore()) {
                reader.readLine();
                continue;
            }

            if(LexerUtils.exitBlock(nameTokens, reader)){
                break;
            }

            Tokens currentTokens = LexerUtils.tokenize(reader);

            tokens = currentTokens.withoutIndent();

            Token label = ParserUtils.getLabel(reader, tokens, errors);

            if (StringUtils.compareNoCase(label, "\\[documentation\\]")) {
                userKeyword.addToken(label.setType(Token.Type.LABEL));
                parseDocumentation(reader, tokens.withoutFirst(), userKeyword);
            }
            else if (StringUtils.compareNoCase(label, "\\[tags\\]")) {
                final NodeList<Literal> tags = ParserUtils.parseTags(label, tokens.withoutFirst());
                userKeyword.setTags(tags);
            }
            else if (StringUtils.compareNoCase(label, "\\[arguments\\]")) {
                NodeList<Variable> arguments = parseArguments(reader, label, tokens.withoutFirst(), errors);
                userKeyword.setArgumentList(arguments);
            }
            else if (StringUtils.compareNoCase(label, "\\[return\\]")) {
                NodeList<Value> returnValues = parseReturn(label, tokens.withoutFirst());
                userKeyword.setReturnVariables(returnValues);
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
        userKeyword.addTokens(tokens.setType(Token.Type.DOCUMENTATION));
        userKeyword.setDocumentation(tokens);
    }

    private static NodeList<Variable> parseArguments(LineReader reader, Token label, Tokens values, ErrorManager errors) {
        NodeList<Variable> arguments = new NodeList<>(label);

        for(Token token: values){
            try {
                arguments.add(Variable.create(token));
            } catch (MalformedVariableException e) {
                errors.registerSyntaxError(
                        reader.getSource(),
                        String.format("%s: %s", ErrorMessages.FAILED_TO_PARSE_PARAMETER, e.getMessage()),
                        Range.fromToken(token, reader.getCurrent())
                );
            }
        }

        return arguments;
    }

    private static NodeList<Value> parseReturn(Token label, Tokens tokens) {
        NodeList<Value> returnValues = new NodeList<>(label.setType(Token.Type.LABEL));

        for(Token token: tokens){
            returnValues.add(ValueParser.parseValue(token));
        }

        return returnValues;
    }

    private static void parseTeardown(LineReader reader, Tokens tokens, UserKeyword userKeyword, ErrorManager errors) throws IOException {
        Step step = StepParser.parse(reader, tokens, false, errors);

        try {
            userKeyword.setTearDown(step);
            userKeyword.addTokens(step.getTokens());
        } catch (InvalidTypeException e) {
            errors.registerSyntaxError(
                    step.getSource(),
                    String.format("%s: %s", ErrorMessages.FAILED_TO_PARSE_TEARDOWN, e.getMessage()),
                    step.getRange()
            );
        }
    }

    private static void parseStep(LineReader reader, Tokens tokens, UserKeyword userKeyword, DynamicImports dynamicImports, ErrorManager errors) throws IOException {
        Step step = StepParser.parse(reader, tokens, false, errors);

        try {
            userKeyword.addStep(step);
            dynamicImports.add(userKeyword, step);
            userKeyword.addTokens(step.getTokens());
        } catch (Exception e) {
           errors.registerSyntaxError(
                   step.getSourceFile().getSource(),
                   ErrorMessages.FAILED_TO_ADD_STEP_TO_KEYWORD,
                   step.getRange()
            );
        }
    }

}
