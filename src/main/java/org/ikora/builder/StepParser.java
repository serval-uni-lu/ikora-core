package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.error.ErrorMessages;
import org.ikora.exception.InvalidDependencyException;
import org.ikora.model.*;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Pattern;

class StepParser {
    private StepParser() {}

    public static Step parse(LineReader reader, Tokens tokens, boolean allowGherkin, ErrorManager errors) throws IOException {
        return parse(reader, tokens, "", allowGherkin, errors);
    }

    public static Step parse(LineReader reader, Tokens tokens, String ignoreTag, boolean allowGherkin, ErrorManager errors) throws IOException {
        Step step;

        Tokens fullTokens = tokens.withoutIndent();
        Tokens tokensWithoutTag = fullTokens.withoutTag(ignoreTag);

        if(isForLoop(reader, tokensWithoutTag, errors)) {
            step = ForLoopParser.parse(reader, errors);
        }
        else if (isAssignment(reader, tokensWithoutTag, errors)){
            step = AssignmentParser.parse(reader, errors);
        }
        else {
            step = KeywordCallParser.parse(reader, tokensWithoutTag, allowGherkin, errors);
        }

        if(step == null){
            step = new InvalidStep(tokensWithoutTag.toString());
            step.setPosition(ParserUtils.getPosition(tokensWithoutTag));
        }

        step.setPosition(ParserUtils.getPosition(tokens));

        return step;
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
