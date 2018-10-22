package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.*;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Linker {
    final static Logger logger = Logger.getLogger(Linker.class);

    private Linker() {}

    static final private Pattern gherkinPattern;

    static {
        gherkinPattern =  Pattern.compile("^(\\s*)(Given|When|Then|And|But)(.)*$", Pattern.CASE_INSENSITIVE);
    }

    static public void link(Project project) throws Exception {
        LibraryResources libraries = project.getLibraries();

        for (TestCaseFile testCaseFile: project.getTestCaseFiles()) {
            for(TestCase testCase: testCaseFile.getTestCases()) {
                linkSteps(testCase, testCaseFile, libraries);
            }

            for(UserKeyword userKeyword: testCaseFile.getElements(UserKeyword.class)) {
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

            Matcher matcher = gherkinPattern.matcher(step.getName());
            String name = matcher.replaceAll("").trim();

            Keyword keyword = getKeyword(name, testCaseFile, libraries);

            if(keyword != null) {
                call.setKeyword(keyword);
                linkStepArguments(call, testCaseFile, libraries);
            }
        }
    }

    private static void linkSteps(UserKeyword userKeyword, TestCaseFile testCaseFile, LibraryResources libraries) throws Exception {
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

            Keyword keyword = getKeyword(name, testCaseFile, libraries);

            if(keyword != null) {
                call.setKeyword(keyword);

                for(Value value : step.getParameters()) {
                    resolveArgument(value, testCaseFile, userKeyword, libraries);
                }

                linkStepArguments(call, testCaseFile, libraries);
            }
        }
    }

    private static void linkStepArguments(KeywordCall step, TestCaseFile testCaseFile, LibraryResources libraries) throws  Exception {
        List<Value> parameters = step.getParameters();

        if(parameters.isEmpty()){
            return;
        }

        for(int position: step.getKeywordsLaunchedPosition()){
            Value keywordParameter = parameters.get(position);

            Keyword keyword = getKeyword(keywordParameter.toString(), testCaseFile, libraries);

            if(keyword != null) {
                KeywordCall call = step.setKeywordParameter(keywordParameter, keyword);
                linkStepArguments(call, testCaseFile, libraries);
            }
        }
    }

    private static Keyword getKeyword(String name, TestCaseFile testCaseFile, LibraryResources libraries) throws Exception{
        Keyword keyword = testCaseFile.findUserKeyword(name);

        if(keyword == null) {
            keyword = libraries.findKeyword(name);
        }

        if(keyword == null) {
            logger.error("Keyword definition for \"" + name + "\" in \"" + testCaseFile.getName() + "\" not found!");
        }

        return keyword;
    }

    static private void resolveArgument(Value value, TestCaseFile testCaseFile, UserKeyword userKeyword, LibraryResources library) throws Exception {
        List<String> variables = value.findVariables();

        for(String name: variables){
            Variable variable = null;

            if(userKeyword != null){
                variable = userKeyword.findLocalVariable(name);
            }

            if(variable != null){
                value.setVariable(name, variable);
            }
            else {
                variable = testCaseFile.findVariable(name);

                if(variable == null){
                    variable = library.findVariable(name);
                }

                if(variable == null) {
                    logger.error("Variable for value \"" + name + "\" in \"" + testCaseFile.getName() + "\" not found!");
                }
                else{
                    value.setVariable(name, variable);
                }
            }
        }
    }
}
