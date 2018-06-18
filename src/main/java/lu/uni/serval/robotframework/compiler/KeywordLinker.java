package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeywordLinker {
    private KeywordLinker() {}

    static public void link(Project project) throws Exception {
        LibraryResources libraries = project.getLibraries();

        for (TestCaseFile testCaseFile: project.getTestCaseFiles()) {
            for(TestCase testCase: testCaseFile.getTestCases()) {
                linkSteps(testCase, testCaseFile, libraries);
            }

            for(UserKeyword userKeyword: testCaseFile.getUserKeywords()) {
                linkSteps(userKeyword, testCaseFile, libraries);
            }
        }
    }

    private static void linkSteps(TestCase testCase, TestCaseFile testCaseFile, LibraryResources libraries) throws Exception {
        for(Step step: testCase) {
            Pattern pattern = Pattern.compile("^(\\s*)(Given|When|Then)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(step.getName().toString());
            String name = matcher.replaceAll("").trim();

            Keyword keyword = testCaseFile.findUserKeyword(name);

            if(keyword == null) {
                keyword = libraries.findKeyword(name);
            }

            if(keyword == null) {
                throw new Exception();
            }

            step.setKeyword(keyword);
        }
    }

    private static void linkSteps(UserKeyword userKeyword, TestCaseFile testCaseFile, LibraryResources libraries) throws Exception {
        for (Step step: userKeyword) {
            String name = step.getName().toString().trim();

            Keyword keyword = testCaseFile.findUserKeyword(name);

            if(keyword == null) {
                keyword = libraries.findKeyword(name);
            }

            if(keyword == null) {
                throw new Exception();
            }


            step.setKeyword(keyword);
        }
    }
}
