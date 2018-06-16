package lu.uni.serval.robotframework.parser;

import lu.uni.serval.robotframework.model.Step;
import lu.uni.serval.robotframework.model.TestCase;
import lu.uni.serval.robotframework.model.TestCaseTable;

import java.io.LineNumberReader;
import java.io.IOException;

public class TestCaseParser {
    private TestCaseParser() {}


    public static String parse(LineNumberReader reader, String test, TestCaseTable testCaseTable) throws IOException {
        String[] tokens = ParsingUtils.tokenizeLine(test);

        TestCase testCase = new TestCase();
        testCase.setName(tokens[0]);

        String line = reader.readLine();

        while(line != null) {
            if(line.equals("")) {
                line = reader.readLine();
                continue;
            }

            tokens = ParsingUtils.tokenizeLine(line);

            if(!tokens[0].equals("")){
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

    private static String parseTimeout(LineNumberReader reader, String[] tokens, TestCase testCase) throws IOException {
        return reader.readLine();
    }

    private static String parseTemplate(LineNumberReader reader, String[] tokens, TestCase testCase) throws IOException {
        return reader.readLine();
    }

    private static String parseTeardown(LineNumberReader reader, String[] tokens, TestCase testCase) throws IOException {
        return reader.readLine();
    }

    private static String parseSetup(LineNumberReader reader, String[] tokens, TestCase testCase) throws IOException {
        return reader.readLine();
    }

    private static String parseTags(LineNumberReader reader, String[] tokens, TestCase testCase) throws IOException {
        tokens = ParsingUtils.removeIndent(tokens);

        for(int i = 1; i < tokens.length; ++i){
            testCase.addTag(tokens[i]);
        }

        return reader.readLine();
    }

    private static String parseDocumentation(LineNumberReader reader, String[] tokens, TestCase testCase) throws IOException {
        StringBuilder builder = new StringBuilder();
        String line = ParsingUtils.parseDocumentation(reader, tokens, builder);

        testCase.setDocumentation(builder.toString());

        return line;
    }

    private static String parseStep(LineNumberReader reader, String[] tokens, TestCase testCase) throws IOException {
        Step step = new Step();
        String line = StepParser.parse(reader, tokens, step);
        testCase.addStep(step);

        return line;
    }
}
