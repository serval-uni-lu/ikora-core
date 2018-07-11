package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.*;
import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class KeywordLinker {
    final static Logger logger = Logger.getLogger(KeywordLinker.class);

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
            if(!(step instanceof KeywordCall)) {
                throw new Exception("expecting a step of type keyword call");
            }

            KeywordCall call = (KeywordCall)step;

            Pattern pattern = Pattern.compile("^(\\s*)(Given|When|Then|And)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(step.getName().toString());
            String name = matcher.replaceAll("").trim();

            Keyword keyword = testCaseFile.findUserKeyword(name);

            if(keyword == null) {
                keyword = libraries.findKeyword(name);
            }

            if(keyword == null) {
                logger.error("Keyword definition for \"" + name + "\" not found!");
            }
            else {
                call.setKeyword(keyword);
            }
        }
    }

    private static void linkSteps(UserKeyword userKeyword, TestCaseFile testCaseFile, LibraryResources libraries) throws Exception {
        for (Step step: userKeyword) {
            if(!(step instanceof KeywordCall)) {
                continue;
            }

            KeywordCall call = (KeywordCall)step;

            String name = step.getName().toString().trim();

            Keyword keyword = testCaseFile.findUserKeyword(name);

            if(keyword == null) {
                keyword = libraries.findKeyword(name);
            }

            if(keyword == null) {
                logger.error("Keyword definition for \"" + name + "\" not found!");
            }
            else {
                call.setKeyword(keyword);
            }
        }
    }
}
