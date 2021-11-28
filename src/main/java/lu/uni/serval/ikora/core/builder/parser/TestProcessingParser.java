package lu.uni.serval.ikora.core.builder.parser;

import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.error.ErrorMessages;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.utils.StringUtils;

import java.io.IOException;
import java.util.Iterator;

public class TestProcessingParser {
    private TestProcessingParser() {}

    public static boolean is(Token label, Scope scope, TestProcessing.Phase phase){
        String scopeString;

        switch (scope){
            case TEST: scopeString = "test "; break;
            case SUITE: scopeString = "suite "; break;
            default: scopeString = ""; break;
        }

        String phaseString;

        switch (phase){
            case SETUP:  phaseString = "setup"; break;
            case TEARDOWN: phaseString = "teardown"; break;
            case TEMPLATE: phaseString = "template"; break;
            default: return false;
        }

        String expression = scopeString + phaseString;
        expression = scope == Scope.KEYWORD ? "\\[" + expression + "\\]" : expression;

        return StringUtils.matchesIgnoreCase(label, expression);
    }

    public static TestProcessing parse(TestProcessing.Phase phase, LineReader reader, Token label, Iterator<Token> tokenIterator, ErrorManager errors) {
        KeywordCall call;

        if(tokenIterator.hasNext()){
            try {
                final Step step = StepParser.parse(0, reader, tokenIterator.next(), tokenIterator, false, errors);

                if(step instanceof KeywordCall){
                    call = (KeywordCall) step;
                }
                else {
                    errors.registerSyntaxError(
                            reader.getSource(),
                            ErrorMessages.EXPECTED_KEYWORD_CALL,
                            step.getRange()
                    );

                    call = null;
                }

            } catch (IOException e) {
                errors.registerInternalError(
                        reader.getSource(),
                        ErrorMessages.FAILED_TO_PARSE_STEP,
                        Range.fromToken(label)
                );

                call = null;
            }
        }
        else{
            errors.registerSyntaxError(
                    reader.getSource(),
                    ErrorMessages.EXPECTED_KEYWORD_CALL,
                    Range.fromToken(label)
            );

            call = null;
        }

        return new TestProcessing(phase, label, call);
    }
}
