package lu.uni.serval.robotframework.parser;

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

            if (ParsingUtils.compareNoCase(label, "\\[documentation\\]")) {
                line = parseDocumentation(reader, tokens, testCase);
            }
            else if (ParsingUtils.compareNoCase(label, "\\[tags\\]")) {
                line = parseTags(reader, tokens, testCase);
            }
            else if (ParsingUtils.compareNoCase(label, "\\[setup\\]")) {
                line = parseSetup(reader, tokens, testCase);
            }
            else if (ParsingUtils.compareNoCase(label, "\\[teardown\\]")) {
                line = parseTeardown(reader, tokens, testCase);
            }
            else if (ParsingUtils.compareNoCase(label, "\\[template\\]")) {
                line = parseTemplate(reader, tokens, testCase);
            }
            else if (ParsingUtils.compareNoCase(label, "\\[timeout\\]")) {
                line = parseTimeout(reader, tokens, testCase);
            }
            else {
                line = parseStep(reader, tokens, testCase);
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
        tokens = ParsingUtils.removeIndent(tokens);

        for(int i = 1; i < tokens.length; ++i){
            testCase.addTag(tokens[i]);
        }

        return Line.getNextLine(reader);
    }

    private static Line parseDocumentation(LineNumberReader reader, String[] tokens, TestCase testCase) throws IOException {
        StringBuilder builder = new StringBuilder();
        Line line = ParsingUtils.parseDocumentation(reader, tokens, builder);

        testCase.setDocumentation(builder.toString());

        return line;
    }

    private static Line parseStep(LineNumberReader reader, String[] tokens, TestCase testCase) throws IOException {
        Step step = new Step();
        Line line = StepParser.parse(reader, tokens, step);
        testCase.addStep(step);

        return line;
    }
}
