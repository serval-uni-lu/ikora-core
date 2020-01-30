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

            String label = ParserUtils.getLabel(reader, tokens, errors).getText();

            if (LexerUtils.compareNoCase(label, "\\[documentation\\]")) {
                parseDocumentation(reader, testCase);
            }
            else if (LexerUtils.compareNoCase(label, "\\[tags\\]")) {
                parseTags(reader, tokens, testCase);
            }
            else if (LexerUtils.compareNoCase(label, "\\[setup\\]")) {
                parseSetup(reader, tokens, testCase, dynamicImports, errors);
            }
            else if (LexerUtils.compareNoCase(label, "\\[teardown\\]")) {
                parseTeardown(reader, tokens, testCase, dynamicImports, errors);
            }
            else if (LexerUtils.compareNoCase(label, "\\[template\\]")) {
                parseTemplate(reader, tokens, testCase, errors);
            }
            else if (LexerUtils.compareNoCase(label, "\\[timeout\\]")) {
                ParserUtils.parseTimeOut("\\[timeout\\]", reader, tokens, testCase, errors);
            }
            else {
                parseStep(reader, tokens, testCase, dynamicImports, errors);
            }
        }

        testCase.setPosition(ParserUtils.getPosition(testTokens));

        return testCase;
    }

    private static void parseTemplate(LineReader reader, Tokens tokens, TestCase testCase, ErrorManager errors) throws IOException {
        Step step = StepParser.parse(reader, tokens, "\\[template\\]", false, errors);

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
        Step step = StepParser.parse(reader, tokens, "\\[teardown\\]", false, errors);

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
        Step step = StepParser.parse(reader, tokens, "\\[setup\\]", false, errors);

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

    private static void parseDocumentation(LineReader reader, TestCase testCase) throws IOException {
        StringBuilder builder = new StringBuilder();
        LexerUtils.parseDocumentation(reader, builder);

        testCase.setDocumentation(builder.toString());
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
