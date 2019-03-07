package org.ukwikora.compiler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ukwikora.analytics.FindSuiteVisitor;
import org.ukwikora.analytics.FindTestCaseVisitor;
import org.ukwikora.analytics.PathMemory;
import org.ukwikora.exception.MissingKeywordException;
import org.ukwikora.model.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Linker {
    private static final Logger logger = LogManager.getLogger(Linker.class);

    private final StaticRuntime runtime;

    private Linker(StaticRuntime runtime) {
        this.runtime = runtime;
    }

    private static final Pattern gherkinPattern;

    static {
        gherkinPattern =  Pattern.compile("^(\\s*)(Given|When|Then|And|But)", Pattern.CASE_INSENSITIVE);
    }

    public static void link(StaticRuntime runtime) throws Exception {
        Linker linker = new Linker(runtime);
        linker.link();
    }

    private void link() throws Exception {
        List<ScopeValue> unresolvedArguments = new ArrayList<>();

        for (TestCaseFile testCaseFile: runtime.getTestCaseFiles()) {
            for(TestCase testCase: testCaseFile.getTestCases()) {
                unresolvedArguments.addAll(linkSteps(testCase, testCaseFile));
            }

            for(UserKeyword userKeyword: testCaseFile.getUserKeywords()) {
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

            unresolvedArguments.addAll(resolveCall(testCase, testCaseFile, call, name));
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

            unresolvedArguments.addAll(resolveCall(userKeyword, testCaseFile, call, name));
        }

        return unresolvedArguments;
    }

    private List<ScopeValue> resolveCall(KeywordDefinition parentKeyword, TestCaseFile testCaseFile, KeywordCall call, String name) throws Exception {
        Keyword keyword = getKeyword(name, testCaseFile);

        if(keyword != null) {
            call.setKeyword(keyword);
            return linkCall(parentKeyword, testCaseFile, call);
        }

        return Collections.emptyList();
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
            try {
                final Optional<Value> parameter = step.getParameter(position, true);

                if(!parameter.isPresent()){
                    throw new MissingKeywordException(String.format("Failed to get keyword at position %d for step %s in file %s",
                            position,
                            step.getName(),
                            step.getFileName()));
                }

                final Value keywordParameter = parameter.get();
                final String parameterName = step.getParameter(position, false).toString();
                Keyword keyword = getKeyword(parameterName, testCaseFile);

                if(keyword == null) {
                    throw new MissingKeywordException(String.format("Failed to find keyword parameter: %s", keywordParameter.getName()));
                }

                KeywordCall call = step.setKeywordParameter(keywordParameter, keyword);

                if(call == null){
                    throw new MissingKeywordException(String.format("Failed to set keyword parameter: %s", keywordParameter.getName()));
                }

                linkStepArguments(call, testCaseFile);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    private Keyword getKeyword(String fullName, TestCaseFile testCaseFile) throws Exception{
        List<String> particles = Arrays.asList(fullName.split("\\."));
        String library = particles.size() > 1 ? String.join(".", particles.subList(0, particles.size() - 1)) : "";
        String name = particles.get(particles.size() - 1);

        Keyword keyword = testCaseFile.findUserKeyword(library, name);

        if(keyword == null) {
            keyword = runtime.getLibraries().findKeyword(library, name);
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

        Set<TestCase> getTestCases(){
            FindTestCaseVisitor visitor = new FindTestCaseVisitor();
            this.keyword.accept(visitor, new PathMemory());

            return visitor.getTestCases();
        }

        Set<String> getSuites(){
            FindSuiteVisitor visitor = new FindSuiteVisitor();
            this.keyword.accept(visitor, new PathMemory());

            return visitor.getSuites();
        }
    }
}
