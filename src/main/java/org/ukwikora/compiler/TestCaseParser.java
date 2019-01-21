package org.ukwikora.compiler;

import org.ukwikora.model.LineRange;
import org.ukwikora.model.Step;
import org.ukwikora.model.TestCase;

import java.io.IOException;

class TestCaseParser {
    private TestCaseParser() {}


    public static TestCase parse(LineReader reader) throws IOException {
        TestCase testCase = new TestCase();
        int startLine = reader.getCurrent().getNumber();

        Line test = reader.getCurrent();
        String[] tokens = LexerUtils.tokenize(test.getText());
        testCase.setName(tokens[0]);

        Line line = reader.readLine();

        while(line.isValid() && LexerUtils.isInBlock(test.getText(), line.getText())) {
            if(line.ignore()) {
                line = reader.readLine();
                continue;
            }

            tokens = LexerUtils.removeIndent(LexerUtils.tokenize(line.getText()));

            String label = tokens[0].trim();

            if (LexerUtils.compareNoCase(label, "\\[documentation\\]")) {
                parseDocumentation(reader, tokens, testCase);
            }
            else if (LexerUtils.compareNoCase(label, "\\[tags\\]")) {
                parseTags(reader, tokens, testCase);
            }
            else if (LexerUtils.compareNoCase(label, "\\[setup\\]")) {
                parseSetup(reader, tokens, testCase);
            }
            else if (LexerUtils.compareNoCase(label, "\\[teardown\\]")) {
                parseTeardown(reader, tokens, testCase);
            }
            else if (LexerUtils.compareNoCase(label, "\\[template\\]")) {
                parseTemplate(reader, tokens, testCase);
            }
            else if (LexerUtils.compareNoCase(label, "\\[timeout\\]")) {
                parseTimeout(reader, tokens, testCase);
            }
            else {
                parseStep(reader, testCase);
            }

            line = reader.getCurrent();
        }

        int endLine = reader.getCurrent().getNumber();
        testCase.setLineRange(new LineRange(startLine, endLine));

        return testCase;
    }

    private static void parseTimeout(LineReader reader, String[] tokens, TestCase testCase) throws IOException {
       reader.readLine();
    }

    private static void parseTemplate(LineReader reader, String[] tokens, TestCase testCase) throws IOException {
        reader.readLine();
    }

    private static void parseTeardown(LineReader reader, String[] tokens, TestCase testCase) throws IOException {
        reader.readLine();
    }

    private static void parseSetup(LineReader reader, String[] tokens, TestCase testCase) throws IOException {
        reader.readLine();
    }

    private static void parseTags(LineReader reader, String[] tokens, TestCase testCase) throws IOException {
        tokens = LexerUtils.removeIndent(tokens);

        for(int i = 1; i < tokens.length; ++i){
            testCase.addTag(tokens[i]);
        }

        reader.readLine();
    }

    private static void parseDocumentation(LineReader reader, String[] tokens, TestCase testCase) throws IOException {
        StringBuilder builder = new StringBuilder();
        LexerUtils.parseDocumentation(reader, tokens, builder);

        testCase.setDocumentation(builder.toString());
    }

    private static void parseStep(LineReader reader, TestCase testCase) throws IOException {
        Step step = StepParser.parse(reader);
        testCase.addStep(step);
    }
}
