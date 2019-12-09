package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.model.LineRange;
import org.ikora.model.Step;
import org.ikora.model.TestCase;

import java.io.IOException;

class TestCaseParser {
    private TestCaseParser() {}


    public static TestCase parse(LineReader reader, DynamicImports dynamicImports, ErrorManager errors) throws IOException {
        TestCase testCase = new TestCase();
        int startLine = reader.getCurrent().getNumber();

        Tokens testTokens = LexerUtils.tokenize(reader.getCurrent().getText());

        ParserUtils.parseName(reader, testTokens, testCase, errors);

        while(reader.getCurrent().isValid()) {
            if(reader.getCurrent().ignore()) {
                reader.readLine();
                continue;
            }

            Tokens tokens = LexerUtils.tokenize(reader.getCurrent().getText());

            if(!testTokens.isParent(tokens)){
                break;
            }

            tokens = tokens.withoutIndent();

            String label = ParserUtils.getLabel(reader, tokens, errors);

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
                parseTemplate(reader, tokens, testCase);
            }
            else if (LexerUtils.compareNoCase(label, "\\[timeout\\]")) {
                parseTimeout(reader, tokens, testCase);
            }
            else {
                parseStep(reader, tokens, testCase, dynamicImports, errors);
            }
        }

        int endLine = reader.getCurrent().getNumber();
            testCase.setLineRange(new LineRange(startLine, endLine));

        return testCase;
    }

    private static void parseTimeout(LineReader reader, Tokens tokens, TestCase testCase) throws IOException {
       reader.readLine();
    }

    private static void parseTemplate(LineReader reader, Tokens tokens, TestCase testCase) throws IOException {
        reader.readLine();
    }

    private static void parseTeardown(LineReader reader, Tokens tokens, TestCase testCase, DynamicImports dynamicImports, ErrorManager errors) throws IOException {
            Step step = StepParser.parse(reader, tokens, "\\[teardown\\]", errors);
            testCase.setTearDown(step);

            dynamicImports.add(testCase, step);
    }

    private static void parseSetup(LineReader reader, Tokens tokens, TestCase testCase, DynamicImports dynamicImports, ErrorManager errors) throws IOException {
            Step step = StepParser.parse(reader, tokens, "\\[setup\\]", errors);
            testCase.setSetup(step);

            dynamicImports.add(testCase, step);
    }

    private static void parseTags(LineReader reader, Tokens tokens, TestCase testCase) throws IOException {
        tokens = tokens.withoutIndent();

        for(Token token: tokens){
            testCase.addTag(token.getValue());
        }

        reader.readLine();
    }

    private static void parseDocumentation(LineReader reader, TestCase testCase) throws IOException {
        StringBuilder builder = new StringBuilder();
        LexerUtils.parseDocumentation(reader, builder);

        testCase.setDocumentation(builder.toString());
    }

    private static void parseStep(LineReader reader, Tokens tokens, TestCase testCase, DynamicImports dynamicImports, ErrorManager errors) throws IOException {
        Step step = StepParser.parse(reader, tokens, errors);

        try {
            testCase.addStep(step);
        } catch (Exception e) {
            errors.registerSyntaxError(
                    "Failed to add step to test case",
                    step.getFile().getFile(),
                    step.getLineRange()
            );
        }

        dynamicImports.add(testCase, step);
    }
}
