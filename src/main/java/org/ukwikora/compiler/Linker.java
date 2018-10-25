package org.ukwikora.compiler;

import org.apache.log4j.Logger;
import org.ukwikora.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Linker {
    final static Logger logger = Logger.getLogger(Linker.class);

    private Linker() {}

    static final private Pattern gherkinPattern;

    static {
        gherkinPattern =  Pattern.compile("^(\\s*)(Given|When|Then|And|But)", Pattern.CASE_INSENSITIVE);
    }

    static public void link(StaticRuntime runtime) throws Exception {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        for (TestCaseFile testCaseFile: runtime.getTestCaseFiles()) {
            for(TestCase testCase: testCaseFile.getTestCases()) {
                unresolvedArguments.addAll(linkSteps(testCase, testCaseFile, runtime));
            }

            for(UserKeyword userKeyword: testCaseFile.getElements(UserKeyword.class)) {
                unresolvedArguments.addAll(linkSteps(userKeyword, testCaseFile, runtime));
            }
        }

        for(ScopeValue valueScope: unresolvedArguments){
            Optional<Variable> variable = runtime.findInScope(valueScope.testCases, valueScope.suites, valueScope.variableName);

            if(variable.isPresent()){
                valueScope.value.setVariable(valueScope.variableName, variable.get());
            }
            else{
                logger.error("Variable for value \"" + valueScope.variableName + "\" in \"" + valueScope.keyword.getName() + "\" not found!");
            }
        }
    }

    private static List<ScopeValue> linkSteps(TestCase testCase, TestCaseFile testCaseFile, StaticRuntime runtime) throws Exception {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        for(Step step: testCase) {
            if(!(step instanceof KeywordCall)) {
                throw new Exception("expecting a step of type keyword call");
            }

            KeywordCall call = (KeywordCall)step;

            Matcher matcher = gherkinPattern.matcher(step.getName());
            String name = matcher.replaceAll("").trim();

            Keyword keyword = getKeyword(name, testCaseFile, runtime);

            if(keyword != null) {
                call.setKeyword(keyword);

                for(Value value : step.getParameters()) {
                    unresolvedArguments.addAll(resolveArgument(value, testCaseFile, testCase, runtime));
                }

                linkStepArguments(call, testCaseFile, runtime);

                runtime.resolveGlobal(call);
            }
        }

        return unresolvedArguments;
    }

    private static List<ScopeValue> linkSteps(UserKeyword userKeyword, TestCaseFile testCaseFile, StaticRuntime runtime) throws Exception {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

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

            Keyword keyword = getKeyword(name, testCaseFile, runtime);

            if(keyword != null) {
                call.setKeyword(keyword);

                for(Value value : step.getParameters()) {
                    unresolvedArguments.addAll(resolveArgument(value, testCaseFile, userKeyword, runtime));
                }

                linkStepArguments(call, testCaseFile, runtime);

                runtime.resolveGlobal(call);
            }
        }

        return unresolvedArguments;
    }

    private static void linkStepArguments(KeywordCall step, TestCaseFile testCaseFile, StaticRuntime runtime) {
        if(!step.hasParameters()){
            return;
        }

        for(int position: step.getKeywordsLaunchedPosition()){
            step.getParameter(position, true).ifPresent(keywordParameter ->{
                try {
                    Keyword keyword = getKeyword(keywordParameter.toString(), testCaseFile, runtime);

                    if(keyword != null) {
                        KeywordCall call = step.setKeywordParameter(keywordParameter, keyword);
                        linkStepArguments(call, testCaseFile, runtime);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private static Keyword getKeyword(String name, TestCaseFile testCaseFile, StaticRuntime project) throws Exception{
        Keyword keyword = testCaseFile.findUserKeyword(name);

        if(keyword == null) {
            keyword = project.getLibraries().findKeyword(name);
        }

        if(keyword == null) {
            logger.error("Keyword definition for \"" + name + "\" in \"" + testCaseFile.getName() + "\" not found!");
        }

        return keyword;
    }

    static private  List<ScopeValue> resolveArgument(Value value, TestCaseFile testCaseFile, TestCase testCase, StaticRuntime runtime) {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        List<String> variables = value.findVariables();

        for(String name: variables){
            Variable variable;

            variable = testCaseFile.findVariable(name);
            if(variable != null){
                value.setVariable(name, variable);
                continue;
            }

            Optional<Variable> option = runtime.findInScope(Collections.singletonList(testCase), testCase.getSuites(), name);
            if(option.isPresent()){
                value.setVariable(name, option.get());
                continue;
            }

            variable = runtime.findLibraryVariable(name);
            if(variable != null){
                value.setVariable(name, variable);
                continue;
            }

            unresolvedArguments.add(new ScopeValue(testCase, value, name));
        }

        return unresolvedArguments;
    }

    static private  List<ScopeValue> resolveArgument(Value value, TestCaseFile testCaseFile, UserKeyword userKeyword, StaticRuntime runtime) {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

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
                Optional<Variable> optional = runtime.findTestVariable(test, name);
                if(optional.isPresent()){
                    variable = optional.get();
                    value.setVariable(name, variable);
                    break;
                }
            }

            if(variable != null){
                continue;
            }

            Optional<Variable> optional = runtime.findInScope(userKeyword.getTestCases(), userKeyword.getSuites(), name);
            if(optional.isPresent()){
                value.setVariable(name, optional.get());
                continue;
            }

            variable = runtime.findLibraryVariable(name);
            if(variable != null){
                value.setVariable(name, variable);
                continue;
            }

            unresolvedArguments.add(new ScopeValue(userKeyword, value, name));
        }

        return unresolvedArguments;
    }
}

class ScopeValue {
    Value value;
    String variableName;
    KeywordDefinition keyword;
    List<TestCase> testCases;
    List<String> suites;

    ScopeValue(KeywordDefinition keyword, Value value, String variableName){
        this.value = value;
        this.variableName = variableName;
        this.keyword = keyword;
        this.testCases = keyword.getTestCases();
        this.suites = keyword.getSuites();
    }
}
