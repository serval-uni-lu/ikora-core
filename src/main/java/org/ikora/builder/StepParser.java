package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.error.ErrorMessages;
import org.ikora.exception.InvalidDependencyException;
import org.ikora.model.*;

import java.io.IOException;
import java.util.Optional;

class StepParser {
    private StepParser() {}

    public static Step parse(LineReader reader, Tokens tokens, ErrorManager errors) throws IOException {
        return parse(reader, tokens, "", errors);
    }

    public static Step parse(LineReader reader, Tokens tokens, String ignoreTag, ErrorManager errors) throws IOException {
        Step step;

        Tokens fullTokens = tokens.withoutIndent();
        Tokens tokensWithoutTag = fullTokens.withoutTag(ignoreTag);

        if(isForLoop(reader, tokensWithoutTag, errors)) {
            step = parseForLoop(reader);
        }
        else if (isAssignment(reader, tokensWithoutTag, errors)){
            step = parseAssignment(reader, errors);
        }
        else {
            step = parseKeywordCall(reader, tokensWithoutTag, errors);
        }

        step.setPosition(ParserUtils.getPosition(tokens));

        return step;
    }

    private static Step parseForLoop(LineReader reader) throws IOException {
        ForLoop forLoop = new ForLoop();
        Tokens loop = LexerUtils.tokenize(reader.getCurrent());

        while (reader.readLine().isValid()){
            Tokens tokens = LexerUtils.tokenize(reader.getCurrent());

            if(!loop.isParent(tokens)){
                break;
            }
        }

        return forLoop;
    }

    private static Step parseAssignment(LineReader reader, ErrorManager errors) throws IOException {
        Assignment assignment = new Assignment(reader.getCurrent().getText());
        Tokens tokens = LexerUtils.tokenize(reader.getCurrent()).withoutIndent();

        int offset = 0;
        boolean leftSide = true;
        for(Token token: tokens){
            try {
                String value = token.getValue().replaceAll("(\\s*)=(\\s*)$", "");

                if(!value.isEmpty()){
                    if(leftSide && Value.isVariable(value)){
                        Optional<Variable> variable = VariableParser.parse(value);

                        if(variable.isPresent()){
                            variable.get().setAssignment(assignment);
                            variable.get().setPosition(ParserUtils.getPosition(token, token));
                            assignment.addReturnValue(variable.get());
                        }
                    }
                    else if(!leftSide){
                        KeywordCall call = getKeywordCall(reader, tokens.withoutFirst(offset), errors);

                        if(call != null){
                            assignment.setExpression(call);
                        }

                        break;
                    }
                }

                leftSide &= !token.getValue().contains("=");
                ++offset;
            } catch (InvalidDependencyException e) {
                errors.registerInternalError(
                        reader.getFile(),
                        ErrorMessages.FAILED_TO_CREATE_DEPENDENCY,
                        ParserUtils.getPosition(token, token)
                );
            }
            ++offset;
        }

        reader.readLine();

        return assignment;
    }

    private static Step parseKeywordCall(LineReader reader, Tokens tokens, ErrorManager errors) throws IOException {
        KeywordCall call = getKeywordCall(reader, tokens, errors);
        reader.readLine();
        return call;
    }

    private static KeywordCall getKeywordCall(LineReader reader, Tokens tokens, ErrorManager errors) {
        Optional<Token> first =  tokens.first();

        if(!first.isPresent()){
            errors.registerInternalError(
                    reader.getFile(),
                    ErrorMessages.EMPTY_TOKEN_SHOULD_BE_KEYWORD,
                    ParserUtils. getPosition(reader.getCurrent())
            );
        }
        else{
            KeywordCall call = new KeywordCall(first.get().getValue());

            for(Token token: tokens.withoutFirst()) {
                try {
                    Argument argument = new Argument(call, token.getValue());
                    argument.setPosition(ParserUtils.getPosition(token, token));

                    call.addArgument(argument);
                } catch (InvalidDependencyException e) {
                    errors.registerInternalError(
                            reader.getFile(),
                            "Failed to register parameter to keyword call",
                            ParserUtils. getPosition(token, token)
                    );
                }
            }

            call.setPosition(ParserUtils.getPosition(tokens.withoutIndent()));
            return call;
        }

        return null;
    }

    private static boolean isAssignment(LineReader reader, Tokens tokens, ErrorManager errors){
        Optional<Token> first = tokens.first();

        if(!first.isPresent()) {
            errors.registerInternalError(
                    reader.getFile(),
                    ErrorMessages.EMPTY_TOKEN_SHOULD_BE_KEYWORD,
                    ParserUtils. getPosition(reader.getCurrent())
            );

            return false;
        }

        return first.get().isAssignment();
    }

    private static boolean isForLoop(LineReader reader, Tokens tokens, ErrorManager errors) {
        Optional<Token> first =  tokens.first();

        if(!first.isPresent()){
            errors.registerInternalError(
                    reader.getFile(),
                    ErrorMessages.EMPTY_TOKEN_SHOULD_BE_KEYWORD,
                    ParserUtils. getPosition(reader.getCurrent())
            );

            return false;
        }

        return first.get().isForLoop();
    }
}
