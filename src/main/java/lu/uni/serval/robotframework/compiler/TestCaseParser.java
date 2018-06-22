package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.Step;
import lu.uni.serval.robotframework.model.TestCase;
import lu.uni.serval.robotframework.model.TestCaseTable;

import java.io.LineNumberReader;
import java.io.IOException;

public class TestCaseParser {
    private TestCaseParser() {}


    public static Line parse(LineNumberReader reader, Line test, TestCaseTable testCaseTable) throws IOException {
        String[] tokens = test.tokenize();

        TestCase testCase = new TestCase();
        testCase.setName(tokens[0]);

        Line line = Line.getNextLine(reader);

        while(line.isValid()) {
            if(line.isEmpty()) {
                line = Line.getNextLine(reader);
                continue;
            }

            tokens = line.tokenize();

            if(!tokens[0].isEmpty()){
                break;
            }

            if(tokens.length < 2) {
                continue;
            }

            String label = tokens[1];

            if (Utils.compareNoCase(label, "\\[documentation\\]")) {
                line = parseDocumentation(reader, tokens, testCase);
            }
            else if (Utils.compareNoCase(label, "\\[tags\\]")) {
                line = parseTags(reader, tokens, testCase);
            }
            else if (Utils.compareNoCase(label, "\\[setup\\]")) {
                line = parseSetup(reader, tokens, testCase);
            }
            else if (Utils.compareNoCase(label, "\\[teardown\\]")) {
                line = parseTeardown(reader, tokens, testCase);
            }
            else if (Utils.compareNoCase(label, "\\[template\\]")) {
                line = parseTemplate(reader, tokens, testCase);
            }
            else if (Utils.compareNoCase(label, "\\[timeout\\]")) {
                line = parseTimeout(reader, tokens, testCase);
            }
            else {
                line = parseStep(reader, line, testCase);
            }
        }

        testCaseTable.add(testCase);

        return line;
    }

    private static Line parseTimeout(LineNumberReader reader, String[] tokens, TestCase testCase) throws IOException {
        return Line.getNextLine(reader);
    }

    private static Line parseTemplate(LineNumberReader reader, String[] tokens, TestCase testCase) throws IOException {
        return Line.getNextLine(reader);
    }

    private static Line parseTeardown(LineNumberReader reader, String[] tokens, TestCase testCase) throws IOException {
        return Line.getNextLine(reader);
    }

    private static Line parseSetup(LineNumberReader reader, String[] tokens, TestCase testCase) throws IOException {
        return Line.getNextLine(reader);
    }

    private static Line parseTags(LineNumberReader reader, String[] tokens, TestCase testCase) throws IOException {
        tokens = Utils.removeIndent(tokens);

        for(int i = 1; i < tokens.length; ++i){
            testCase.addTag(tokens[i]);
        }

        return Line.getNextLine(reader);
    }

    private static Line parseDocumentation(LineNumberReader reader, String[] tokens, TestCase testCase) throws IOException {
        StringBuilder builder = new StringBuilder();
        Line line = Utils.parseDocumentation(reader, tokens, builder);

        testCase.setDocumentation(builder.toString());

        return line;
    }

    private static Line parseStep(LineNumberReader reader, Line line, TestCase testCase) throws IOException {
        Step step = null;
        line = StepParser.parse(reader, line, step);
        testCase.addStep(step);

        return line;
    }
}
