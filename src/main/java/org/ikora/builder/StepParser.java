package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.error.ErrorMessages;
import org.ikora.model.*;

import java.io.IOException;
import java.util.Optional;

class StepParser {
    private StepParser() {}

    public static Step parse(LineReader reader, Tokens tokens, boolean allowGherkin, ErrorManager errors) throws IOException {
        Step step;

        Tokens fullTokens = tokens.withoutIndent();

        if(isForLoop(reader, fullTokens, errors)) {
            step = ForLoopParser.parse(reader, errors);
        }
        else if (isAssignment(reader, fullTokens, errors)){
            step = AssignmentParser.parse(reader, errors);
        }
        else {
            step = KeywordCallParser.parse(reader, fullTokens, allowGherkin, errors);
        }

        if(step == null){
            step = new InvalidStep(fullTokens.first());
        }

        return step;
    }

    private static boolean isAssignment(LineReader reader, Tokens tokens, ErrorManager errors){
        if(tokens.isEmpty()) {
            errors.registerInternalError(
                    reader.getFile(),
                    ErrorMessages.EMPTY_TOKEN_SHOULD_BE_KEYWORD,
                    Position.fromLine(reader.getCurrent())
            );

            return false;
        }

        return tokens.first().isAssignment();
    }

    private static boolean isForLoop(LineReader reader, Tokens tokens, ErrorManager errors) {
        if(tokens.isEmpty()){
            errors.registerInternalError(
                    reader.getFile(),
                    ErrorMessages.EMPTY_TOKEN_SHOULD_BE_KEYWORD,
                    Position.fromLine(reader.getCurrent())
            );

            return false;
        }

        return tokens.first().isForLoop();
    }
}
