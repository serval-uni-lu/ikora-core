package lu.uni.serval.robotframework.parser;

import lu.uni.serval.robotframework.model.Step;
import lu.uni.serval.robotframework.model.TestCase;
import lu.uni.serval.robotframework.model.TestCaseTable;

import java.io.BufferedReader;
import java.io.IOException;

public class TestCaseParser {
    private TestCaseParser() {}


    public static String parse(BufferedReader bufferedReader, String test, TestCaseTable testCaseTable) throws IOException {
        String[] tokens = ParsingUtils.tokenizeLine(test);

        TestCase testCase = new TestCase();
        testCase.setName(tokens[0]);

        String line = bufferedReader.readLine();

        while(line != null) {
            if(line.equals("")) {
                line = bufferedReader.readLine();
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
                line = parseDocumentation(bufferedReader, tokens, testCase);
            }
            else if (ParsingUtils.compareNoCase(label, "\\[tags\\]")) {
                line = parseTags(bufferedReader, tokens, testCase);
            }
            else if (ParsingUtils.compareNoCase(label, "\\[setup\\]")) {
                line = parseSetup(bufferedReader, tokens, testCase);
            }
            else if (ParsingUtils.compareNoCase(label, "\\[teardown\\]")) {
                line = parseTeardown(bufferedReader, tokens, testCase);
            }
            else if (ParsingUtils.compareNoCase(label, "\\[template\\]")) {
                line = parseTemplate(bufferedReader, tokens, testCase);
            }
            else if (ParsingUtils.compareNoCase(label, "\\[timeout\\]")) {
                line = parseTimeout(bufferedReader, tokens, testCase);
            }
            else {
                line = parseStep(bufferedReader, tokens, testCase);
            }
        }

        testCaseTable.add(testCase);

        return line;
    }

    private static String parseTimeout(BufferedReader bufferedReader, String[] tokens, TestCase testCase) throws IOException {
        return bufferedReader.readLine();
    }

    private static String parseTemplate(BufferedReader bufferedReader, String[] tokens, TestCase testCase) throws IOException {
        return bufferedReader.readLine();
    }

    private static String parseTeardown(BufferedReader bufferedReader, String[] tokens, TestCase testCase) throws IOException {
        return bufferedReader.readLine();
    }

    private static String parseSetup(BufferedReader bufferedReader, String[] tokens, TestCase testCase) throws IOException {
        return bufferedReader.readLine();
    }

    private static String parseTags(BufferedReader bufferedReader, String[] tokens, TestCase testCase) throws IOException {
        tokens = ParsingUtils.removeIndent(tokens);

        for(int i = 1; i < tokens.length; ++i){
            testCase.addTag(tokens[i]);
        }

        return bufferedReader.readLine();
    }

    private static String parseDocumentation(BufferedReader bufferedReader, String[] tokens, TestCase testCase) throws IOException {
        StringBuilder builder = new StringBuilder();
        String line = ParsingUtils.parseDocumentation(bufferedReader, tokens, builder);

        testCase.setDocumentation(builder.toString());

        return line;
    }

    private static String parseStep(BufferedReader bufferedReader, String[] tokens, TestCase testCase) throws IOException {
        Step step = new Step();
        String line = StepParser.parse(bufferedReader, tokens, step);
        testCase.addStep(step);

        return line;
    }
}
