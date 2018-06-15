package lu.uni.serval.robotframework.parser;

import lu.uni.serval.robotframework.model.*;

public class KeywordLinker {
    private KeywordLinker() {}

    static public void link(Project project) {
        for (TestCaseFile testCaseFile: project.getTestCaseFiles()) {
            for(TestCase testCase: testCaseFile.getTestCases()) {
                linkSteps(testCase, testCaseFile);
            }
        }
    }

    private static void linkSteps(TestCase testCase, TestCaseFile testCaseFile) {
        for(Step step: testCase) {
            KeywordDefinition keyword = testCaseFile.findKeyword(step);
            step.setKeyword(keyword);
        }
    }
}
