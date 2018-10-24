package org.ukwikora.compiler;

import org.apache.log4j.Logger;
import org.ukwikora.model.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Linker {
    final static Logger logger = Logger.getLogger(Linker.class);

    private Linker() {}

    static final private Pattern gherkinPattern;

    static {
        gherkinPattern =  Pattern.compile("^(\\s*)(Given|When|Then|And|But)", Pattern.CASE_INSENSITIVE);
    }

    static public void link(Project project) throws Exception {
        for (TestCaseFile testCaseFile: project.getTestCaseFiles()) {
            for(TestCase testCase: testCaseFile.getTestCases()) {
                linkSteps(testCase, testCaseFile, project);
            }

            for(UserKeyword userKeyword: testCaseFile.getElements(UserKeyword.class)) {
                linkSteps(userKeyword, testCaseFile, project);
            }
        }
    }

    private static void linkSteps(TestCase testCase, TestCaseFile testCaseFile, Project project) throws Exception {
        for(Step step: testCase) {
            if(!(step instanceof KeywordCall)) {
                throw new Exception("expecting a step of type keyword call");
            }

            KeywordCall call = (KeywordCall)step;

            Matcher matcher = gherkinPattern.matcher(step.getName());
            String name = matcher.replaceAll("").trim();

            Keyword keyword = getKeyword(name, testCaseFile, project);

            if(keyword != null) {
                call.setKeyword(keyword);
                linkStepArguments(call, testCaseFile, project);
            }
        }
    }

    private static void linkSteps(UserKeyword userKeyword, TestCaseFile testCaseFile, Project project) throws Exception {
        for (Step step: userKeyword) {
            KeywordCall call;

            if(step instanceof KeywordCall){
                call = (KeywordCall)step;
            }
            else if(step instanceof Assignment){
                call = ((Assignment)step).getExpression();
            }
            else{
                continue;
            }

            String name = call.getName().trim();

            Keyword keyword = getKeyword(name, testCaseFile, project);

            if(keyword != null) {
                call.setKeyword(keyword);

                for(Value value : step.getParameters()) {
                    resolveArgument(value, testCaseFile, userKeyword, project);
                }

                linkStepArguments(call, testCaseFile, project);
            }
        }
    }

    private static void linkStepArguments(KeywordCall step, TestCaseFile testCaseFile, Project project) {
        if(!step.hasParameters()){
            return;
        }

        for(int position: step.getKeywordsLaunchedPosition()){
            step.getParameter(position, true).ifPresent(keywordParameter ->{
                try {
                    Keyword keyword = getKeyword(keywordParameter.toString(), testCaseFile, project);

                    if(keyword != null) {
                        KeywordCall call = step.setKeywordParameter(keywordParameter, keyword);
                        linkStepArguments(call, testCaseFile, project);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private static Keyword getKeyword(String name, TestCaseFile testCaseFile, Project project) throws Exception{
        Keyword keyword = testCaseFile.findUserKeyword(name);

        if(keyword == null) {
            keyword = project.getLibraries().findKeyword(name);
        }

        if(keyword == null) {
            logger.error("Keyword definition for \"" + name + "\" in \"" + testCaseFile.getName() + "\" not found!");
        }

        return keyword;
    }

    static private void resolveArgument(Value value, TestCaseFile testCaseFile, UserKeyword userKeyword, Project project) {
        List<String> variables = value.findVariables();

        for(String name: variables){
            Variable variable;

            if(userKeyword != null){
                variable = userKeyword.findLocalVariable(name);

                if(variable != null){
                    value.setVariable(name, variable);
                    continue;
                }
            }

            variable = testCaseFile.findVariable(name);
            if(variable != null){
                value.setVariable(name, variable);
                continue;
            }

            for(TestCase test: userKeyword.getTestCases()){
                variable = project.findTestVariable(test, name);
                if(variable != null){
                    value.setVariable(name, variable);
                    break;
                }
            }

            if(variable != null){
                continue;
            }

            variable = project.findGlobalVariable(name);
            if(variable != null){
                value.setVariable(name, variable);
                continue;
            }

            variable = project.findLibraryVariable(name);
            if(variable != null){
                value.setVariable(name, variable);
                continue;
            }

            logger.error("Variable for value \"" + name + "\" in \"" + testCaseFile.getName() + "\" not found!");
        }
    }
}
