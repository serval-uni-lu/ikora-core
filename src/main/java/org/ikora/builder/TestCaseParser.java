package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.error.ErrorMessages;
import org.ikora.exception.InvalidTypeException;
import org.ikora.model.Step;
import org.ikora.model.TestCase;
import org.ikora.model.Token;

import java.io.IOException;

class TestCaseParser {
    private TestCaseParser() {}

    public static TestCase parse(LineReader reader, DynamicImports dynamicImports, ErrorManager errors) throws IOException {
        TestCase testCase = new TestCase();

        Tokens testTokens = LexerUtils.tokenize(reader.getCurrent());
        Tokens tokens;

        ParserUtils.parseName(reader, testTokens, testCase, errors);

        while(reader.getCurrent().isValid()) {
            if(reader.getCurrent().ignore()) {
                reader.readLine();
                continue;
            }

            Tokens currentTokens = LexerUtils.tokenize(reader.getCurrent());

            if(!testTokens.isParent(currentTokens)){
                break;
            }

            tokens = currentTokens.withoutIndent();

            Token label = ParserUtils.getLabel(reader, tokens, errors);

            if (LexerUtils.compareNoCase(label, "\\[documentation\\]")) {
                testCase.addToken(label.setType(Token.Type.LABEL));
                parseDocumentation(reader, tokens.withoutTag("\\[documentation\\]"), testCase);
            }
            else if (LexerUtils.compareNoCase(label, "\\[tags\\]")) {
                testCase.addToken(label.setType(Token.Type.LABEL));
                parseTags(reader, tokens, testCase);
            }
            else if (LexerUtils.compareNoCase(label, "\\[setup\\]")) {
                testCase.addToken(label.setType(Token.Type.LABEL));
                parseSetup(reader, tokens.withoutTag("\\[setup\\]"), testCase, dynamicImports, errors);
            }
            else if (LexerUtils.compareNoCase(label, "\\[teardown\\]")) {
                testCase.addToken(label.setType(Token.Type.LABEL));
                parseTeardown(reader, tokens.withoutTag("\\[teardown\\]"), testCase, dynamicImports, errors);
            }
            else if (LexerUtils.compareNoCase(label, "\\[template\\]")) {
                testCase.addToken(label.setType(Token.Type.LABEL));
                parseTemplate(reader, tokens.withoutTag("\\[template\\]"), testCase, errors);
            }
            else if (LexerUtils.compareNoCase(label, "\\[timeout\\]")) {
                testCase.addToken(label.setType(Token.Type.LABEL));
                ParserUtils.parseTimeOut(reader, tokens.withoutTag("\\[timeout\\]"), testCase, errors);
            }
            else {
                parseStep(reader, tokens, testCase, dynamicImports, errors);
            }
        }

        return testCase;
    }

    private static void parseTemplate(LineReader reader, Tokens tokens, TestCase testCase, ErrorManager errors) throws IOException {
        Step step = StepParser.parse(reader, tokens, false, errors);

        try {
            testCase.setTemplate(step);
        } catch (InvalidTypeException e) {
            errors.registerSyntaxError(
                    step.getFile(),
                    String.format("%s: %s", ErrorMessages.FAILED_TO_PARSE_TEMPLATE, e.getMessage()),
                    step.getPosition()
            );
        }
        reader.readLine();
    }

    private static void parseTeardown(LineReader reader, Tokens tokens, TestCase testCase, DynamicImports dynamicImports, ErrorManager errors) throws IOException {
        Step step = StepParser.parse(reader, tokens, false, errors);

        try {
            testCase.setTearDown(step);
        } catch (InvalidTypeException e) {
            errors.registerSyntaxError(
                    step.getFile(),
                    String.format("%s: %s", ErrorMessages.FAILED_TO_PARSE_TEARDOWN, e.getMessage()),
                    step.getPosition()
            );
        }

        dynamicImports.add(testCase, step);
    }

    private static void parseSetup(LineReader reader, Tokens tokens, TestCase testCase, DynamicImports dynamicImports, ErrorManager errors) throws IOException {
        Step step = StepParser.parse(reader, tokens, false, errors);

        try {
            testCase.setSetup(step);
        } catch (InvalidTypeException e) {
            errors.registerSyntaxError(
                    step.getFile(),
                    String.format("%s: %s", ErrorMessages.FAILED_TO_PARSE_SETUP, e.getMessage()),
                    step.getPosition()
            );
        }

        dynamicImports.add(testCase, step);
    }

    private static void parseTags(LineReader reader, Tokens tokens, TestCase testCase) throws IOException {
        tokens = tokens.withoutIndent();

        for(Token token: tokens){
            testCase.addTag(token.getText());
        }

        reader.readLine();
    }

    private static void parseDocumentation(LineReader reader, Tokens tokens, TestCase testCase) throws IOException {
        StringBuilder builder = new StringBuilder();
        final Tokens documentationTokens = LexerUtils.parseMultiLine(reader, tokens, builder);

        testCase.setDocumentation(builder.toString());
        testCase.addTokens(documentationTokens);
    }

    private static void parseStep(LineReader reader, Tokens tokens, TestCase testCase, DynamicImports dynamicImports, ErrorManager errors) throws IOException {
        Step step = StepParser.parse(reader, tokens, true, errors);

        try {
            testCase.addStep(step);
        } catch (Exception e) {
            errors.registerSyntaxError(
                    step.getFile(),
                    ErrorMessages.FAILED_TO_ADD_STEP_TO_TEST_CASE,
                    step.getPosition()
            );
        }

        dynamicImports.add(testCase, step);
    }
}
