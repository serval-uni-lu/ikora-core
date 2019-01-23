package org.ukwikora.compiler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ukwikora.libraries.builtin.SetGlobalVariable;
import org.ukwikora.libraries.builtin.SetSuiteVariable;
import org.ukwikora.libraries.builtin.SetTestVariable;
import org.ukwikora.model.*;
import org.ukwikora.model.Runtime;

import java.util.List;
import java.util.Optional;


public class StaticRuntime extends Runtime {
    private final static Logger logger = LogManager.getLogger(StaticRuntime.class);

    public StaticRuntime(Project project) {
        super(project);
    }

    public void resolveGlobal(KeywordCall call){
        Keyword keyword = call.getKeyword();

        if(!(keyword instanceof LibraryKeyword)){
            return;
        }

        if(keyword.getClass() == SetGlobalVariable.class){
            call.getParameter(0, false).ifPresent(value ->
                createVariable(value).ifPresent(variable ->
                    this.scope.setGlobalScope(variable)
                )
            );
        }
        else if(keyword.getClass() == SetSuiteVariable.class){
            call.getParameter(0, false).ifPresent(value ->
                    createVariable(value).ifPresent(variable -> {
                        for(String suite: call.getSuites()){
                            this.scope.setSuiteScope(suite, variable);
                        }
                    })
            );
        }
        else if(keyword.getClass() == SetTestVariable.class){
            call.getParameter(0, false).ifPresent(value ->
                    createVariable(value).ifPresent(variable -> {
                        for(TestCase testCase: call.getTestCases()){
                            this.scope.setTestScope(testCase, variable);
                        }
                    })
            );
        }
    }

    public Optional<Variable> findInScope(List<TestCase> testCases, List<String> suites, String name){
        return scope.findInScope(testCases, suites, name);
    }

    private Optional<Variable> createVariable(Value value){
        Optional<Variable> variable = VariableParser.parse(value.toString());

        if(variable.isPresent()){
            value.setVariable(value.toString(), variable.get());
        }
        else{
            logger.error("Malformed assignment to global variable for " + value.toString());
        }

        return variable;
    }
}
