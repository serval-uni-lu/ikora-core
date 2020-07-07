package tech.ikora.builder;

import tech.ikora.error.ErrorManager;
import tech.ikora.error.ErrorMessages;
import tech.ikora.model.InvalidStep;
import tech.ikora.model.Range;
import tech.ikora.model.Step;
import tech.ikora.model.Tokens;

import java.io.IOException;

class StepParser {
    private StepParser() {}

    public static Step parse(LineReader reader, Tokens tokens, boolean allowGherkin, ErrorManager errors) throws IOException {
        Step step;

        Tokens fullTokens = tokens.withoutIndent();

        if(isForLoop(reader, fullTokens, errors)) {
            step = ForLoopParser.parse(reader, tokens, errors);
        }
        else if (isAssignment(reader, fullTokens, errors)){
            step = AssignmentParser.parse(reader, tokens, errors);
        }
        else {
            step = KeywordCallParser.parse(reader, tokens, allowGherkin, errors);
        }

        if(step == null){
            step = new InvalidStep(fullTokens.first());
        }

        return step;
    }

    private static boolean isAssignment(LineReader reader, Tokens tokens, ErrorManager errors){
        if(tokens.isEmpty()) {
            errors.registerInternalError(
                    reader.getSource(),
                    ErrorMessages.EMPTY_TOKEN_SHOULD_BE_KEYWORD,
                    Range.fromLine(reader.getCurrent())
            );

            return false;
        }

        return tokens.first().isAssignment();
    }

    private static boolean isForLoop(LineReader reader, Tokens tokens, ErrorManager errors) {
        if(tokens.isEmpty()){
            errors.registerInternalError(
                    reader.getSource(),
                    ErrorMessages.EMPTY_TOKEN_SHOULD_BE_KEYWORD,
                    Range.fromLine(reader.getCurrent())
            );

            return false;
        }

        return tokens.first().isForLoop();
    }
}
