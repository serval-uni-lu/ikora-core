package org.ukwikora.compiler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ukwikora.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Linker {
    private final static Logger logger = LogManager.getLogger(Linker.class);

    final private StaticRuntime runtime;

    private Linker(StaticRuntime runtime) {
        this.runtime = runtime;
    }

    static final private Pattern gherkinPattern;

    static {
        gherkinPattern =  Pattern.compile("^(\\s*)(Given|When|Then|And|But)", Pattern.CASE_INSENSITIVE);
    }

    static public void link(StaticRuntime runtime) throws Exception {
        Linker linker = new Linker(runtime);
        linker.link();
    }

    private void link() throws Exception {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        for (TestCaseFile testCaseFile: runtime.getTestCaseFiles()) {
            for(TestCase testCase: testCaseFile.getTestCases()) {
                unresolvedArguments.addAll(linkSteps(testCase, testCaseFile));
            }

            for(UserKeyword userKeyword: testCaseFile.getStatements(UserKeyword.class)) {
                unresolvedArguments.addAll(linkSteps(userKeyword, testCaseFile));
            }
        }

        processUnresolvedArguments(unresolvedArguments);
    }

    private List<ScopeValue> linkSteps(TestCase testCase, TestCaseFile testCaseFile) throws Exception {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        for(Step step: testCase) {
            if(!(step instanceof KeywordCall)) {
                throw new Exception("expecting a step of type keyword call");
            }

            KeywordCall call = (KeywordCall)step;

            Matcher matcher = gherkinPattern.matcher(step.getName());
            String name = matcher.replaceAll("").trim();

            Keyword keyword = getKeyword(name, testCaseFile);

            if(keyword != null) {
                call.setKeyword(keyword);

                unresolvedArguments.addAll(linkCall(testCase, testCaseFile, call));
            }
        }

        return unresolvedArguments;
    }

    private List<ScopeValue> linkSteps(UserKeyword userKeyword, TestCaseFile testCaseFile) throws Exception {
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

            Keyword keyword = getKeyword(name, testCaseFile);

            if(keyword != null) {
                call.setKeyword(keyword);
                unresolvedArguments.addAll(linkCall(userKeyword, testCaseFile, call));
            }
        }

        return unresolvedArguments;
    }

    private List<ScopeValue> linkCall(KeywordDefinition parentKeyword, TestCaseFile testCaseFile, KeywordCall call) {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        for(Value value : call.getParameters()) {
            unresolvedArguments.addAll(resolveArgument(value, testCaseFile, parentKeyword));
        }

        linkStepArguments(call, testCaseFile);
        runtime.resolveGlobal(call);

        return unresolvedArguments;
    }

    private void linkStepArguments(KeywordCall step, TestCaseFile testCaseFile) {
        if(!step.hasParameters()){
            return;
        }

        for(int position: step.getKeywordsLaunchedPosition()){
            step.getParameter(position, true).ifPresent(keywordParameter ->{
                try {
                    Keyword keyword = getKeyword(keywordParameter.toString(), testCaseFile);

                    if(keyword != null) {
                        KeywordCall call = step.setKeywordParameter(keywordParameter, keyword);
                        linkStepArguments(call, testCaseFile);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private Keyword getKeyword(String name, TestCaseFile testCaseFile) throws Exception{
        Keyword keyword = testCaseFile.findUserKeyword(name);

        if(keyword == null) {
            keyword = runtime.getLibraries().findKeyword(name);
        }

        if(keyword == null) {
            logger.error("Keyword definition for \"" + name + "\" in \"" + testCaseFile.getName() + "\" not found!");
        }

        return keyword;
    }

    private  List<ScopeValue> resolveArgument(Value value, TestCaseFile testCaseFile, KeywordDefinition keyword) {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        List<String> variables = value.findVariables();

        for(String name: variables){
            Variable variable;

            if(keyword.getClass() == UserKeyword.class){
                variable = ((UserKeyword)keyword).findLocalVariable(name);

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

            variable = runtime.findLibraryVariable(name);
            if(variable != null){
                value.setVariable(name, variable);
                continue;
            }

            unresolvedArguments.add(new ScopeValue(keyword, value, name));
        }

        return unresolvedArguments;
    }

    private void processUnresolvedArguments(List<ScopeValue> unresolvedArguments) {
        for(ScopeValue valueScope: unresolvedArguments){
            Optional<Variable> variable = runtime.findInScope(valueScope.getTestCases(), valueScope.getSuites(), valueScope.variableName);

            if(variable.isPresent()){
                valueScope.value.setVariable(valueScope.variableName, variable.get());
            }
            else{
                logger.error("Variable for value \"" + valueScope.variableName + "\" in \"" + valueScope.keyword.getName() + "\" not found!");
            }
        }
    }

    class ScopeValue {
        Value value;
        String variableName;
        KeywordDefinition keyword;

        ScopeValue(KeywordDefinition keyword, Value value, String variableName){
            this.value = value;
            this.variableName = variableName;
            this.keyword = keyword;
        }

        List<TestCase> getTestCases(){
            return this.keyword.getTestCases();
        }

        List<String> getSuites(){
            return this.keyword.getSuites();
        }
    }
}
