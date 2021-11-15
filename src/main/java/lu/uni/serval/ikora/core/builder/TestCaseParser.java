package lu.uni.serval.ikora.core.builder;

import lu.uni.serval.ikora.core.exception.InvalidTypeException;
import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.error.ErrorMessages;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.utils.StringUtils;

import java.io.IOException;
import java.util.Optional;

class TestCaseParser {
    private TestCaseParser() {}

    public static TestCase parse(LineReader reader, Tokens nameTokens, DynamicImports dynamicImports, ErrorManager errors) throws IOException {
        final Optional<TestCase> optionalTestCase = ParserUtils.createKeyword(TestCase.class, reader, nameTokens, errors);

        if(!optionalTestCase.isPresent()){
            throw new IOException(String.format("failed to read test case at line %d in file %s",
                    reader.getCurrent().getNumber(),
                    reader.getSource().getAbsolutePath()));
        }

        final TestCase testCase = optionalTestCase.get();

        while(reader.getCurrent().isValid() && !LexerUtils.exitBlock(nameTokens, reader)) {
            if(reader.getCurrent().ignore()) {
                reader.readLine();
                continue;
            }

            Tokens currentTokens = LexerUtils.tokenize(reader);
            Tokens tokens = currentTokens.withoutIndent();
            Token label = ParserUtils.getLabel(reader, tokens, errors);

            if (DocumentationParser.is(label)) {
                final Documentation documentation = DocumentationParser.parse(label, tokens);
                testCase.setDocumentation(documentation);
            }
            else if (StringUtils.compareNoCase(label, "\\[tags\\]")) {
                final NodeList<Literal> tags = ParserUtils.parseTags(label, tokens);
                testCase.setTags(tags);
            }
            else if (StringUtils.compareNoCase(label, "\\[setup\\]")) {
                testCase.addToken(label.setType(Token.Type.LABEL));
                parseSetup(reader, tokens.withoutTag("\\[setup\\]"), testCase, dynamicImports, errors);
            }
            else if (StringUtils.compareNoCase(label, "\\[teardown\\]")) {
                testCase.addToken(label.setType(Token.Type.LABEL));
                parseTeardown(reader, tokens.withoutTag("\\[teardown\\]"), testCase, dynamicImports, errors);
            }
            else if (StringUtils.compareNoCase(label, "\\[template\\]")) {
                testCase.addToken(label.setType(Token.Type.LABEL));
                parseTemplate(reader, tokens.withoutTag("\\[template\\]"), testCase, errors);
            }
            else if (StringUtils.compareNoCase(label, "\\[timeout\\]")) {
                testCase.addToken(label.setType(Token.Type.LABEL));
                ParserUtils.parseTimeOut(reader, tokens.withoutTag("\\[timeout\\]"), testCase, errors);
            }
            else {
                parseStep(reader, currentTokens, testCase, dynamicImports, errors);
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
                    step.getSource(),
                    String.format("%s: %s", ErrorMessages.FAILED_TO_PARSE_TEMPLATE, e.getMessage()),
                    step.getRange()
            );
        }
    }

    private static void parseTeardown(LineReader reader, Tokens tokens, TestCase testCase, DynamicImports dynamicImports, ErrorManager errors) throws IOException {
        Step step = StepParser.parse(reader, tokens, false, errors);

        try {
            testCase.setTearDown(step);
        } catch (InvalidTypeException e) {
            errors.registerSyntaxError(
                    step.getSource(),
                    String.format("%s: %s", ErrorMessages.FAILED_TO_PARSE_TEARDOWN, e.getMessage()),
                    step.getRange()
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
                    step.getSource(),
                    String.format("%s: %s", ErrorMessages.FAILED_TO_PARSE_SETUP, e.getMessage()),
                    step.getRange()
            );
        }

        dynamicImports.add(testCase, step);
    }

    private static void parseStep(LineReader reader, Tokens tokens, TestCase testCase, DynamicImports dynamicImports, ErrorManager errors) throws IOException {
        Step step = StepParser.parse(reader, tokens, true, errors);

        try {
            testCase.addStep(step);
        } catch (Exception e) {
            errors.registerSyntaxError(
                    step.getSource(),
                    ErrorMessages.FAILED_TO_ADD_STEP_TO_TEST_CASE,
                    step.getRange()
            );
        }

        dynamicImports.add(testCase, step);
    }
}
