package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.LineRange;
import lu.uni.serval.robotframework.model.Step;
import lu.uni.serval.robotframework.model.TestCase;

import java.io.IOException;

public class TestCaseParser {
    private TestCaseParser() {}


    public static TestCase parse(LineReader reader) throws IOException {
        TestCase testCase = new TestCase();
        int startLine = reader.getCurrent().getNumber();

        Line test = reader.getCurrent();
        String[] tokens = test.tokenize();
        testCase.setName(tokens[0]);

        Line line = reader.readLine();

        while(line.isValid() && line.isInBlock(test)) {
            if(line.ignore()) {
                line = reader.readLine();
                continue;
            }

            tokens = Utils.removeIndent(line.tokenize());

            String label = tokens[0].trim();

            if (Utils.compareNoCase(label, "\\[documentation\\]")) {
                parseDocumentation(reader, tokens, testCase);
            }
            else if (Utils.compareNoCase(label, "\\[tags\\]")) {
                parseTags(reader, tokens, testCase);
            }
            else if (Utils.compareNoCase(label, "\\[setup\\]")) {
                parseSetup(reader, tokens, testCase);
            }
            else if (Utils.compareNoCase(label, "\\[teardown\\]")) {
                parseTeardown(reader, tokens, testCase);
            }
            else if (Utils.compareNoCase(label, "\\[template\\]")) {
                parseTemplate(reader, tokens, testCase);
            }
            else if (Utils.compareNoCase(label, "\\[timeout\\]")) {
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
        tokens = Utils.removeIndent(tokens);

        for(int i = 1; i < tokens.length; ++i){
            testCase.addTag(tokens[i]);
        }

        reader.readLine();
    }

    private static void parseDocumentation(LineReader reader, String[] tokens, TestCase testCase) throws IOException {
        StringBuilder builder = new StringBuilder();
        Utils.parseDocumentation(reader, tokens, builder);

        testCase.setDocumentation(builder.toString());
    }

    private static void parseStep(LineReader reader, TestCase testCase) throws IOException {
        Step step = StepParser.parse(reader);
        testCase.addStep(step);
    }
}
