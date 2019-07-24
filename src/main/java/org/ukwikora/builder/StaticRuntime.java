package org.ukwikora.builder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ukwikora.analytics.FindSuiteVisitor;
import org.ukwikora.analytics.FindTestCaseVisitor;
import org.ukwikora.analytics.PathMemory;
import org.ukwikora.libraries.builtin.ImportLibrary;
import org.ukwikora.libraries.builtin.SetGlobalVariable;
import org.ukwikora.libraries.builtin.SetSuiteVariable;
import org.ukwikora.libraries.builtin.SetTestVariable;
import org.ukwikora.model.*;
import org.ukwikora.runner.Runtime;

import java.util.Optional;
import java.util.Set;


public class StaticRuntime extends Runtime {
    private static final Logger logger = LogManager.getLogger(StaticRuntime.class);
    private final DynamicImports dynamicImports;

    public StaticRuntime(Project project, DynamicImports dynamicImports) {
        super(project);
        this.dynamicImports = dynamicImports;
    }

    public void resolveGlobal(KeywordCall call){
        call.getKeyword().ifPresent(
            keyword -> {
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
                                FindSuiteVisitor visitor = new FindSuiteVisitor();
                                call.accept(visitor, new PathMemory());

                                for(String suite: visitor.getSuites()){
                                    this.scope.setSuiteScope(suite, variable);
                                }
                            })
                    );
                }
                else if(keyword.getClass() == SetTestVariable.class){
                    call.getParameter(0, false).ifPresent(value ->
                            createVariable(value).ifPresent(variable -> {
                                FindTestCaseVisitor visitor = new FindTestCaseVisitor();
                                call.accept(visitor, new PathMemory());

                                for(TestCase testCase: visitor.getTestCases()){
                                    this.scope.setTestScope(testCase, variable);
                                }
                            })
                    );
                }
                else if(keyword.getClass() == ImportLibrary.class){
                    KeywordDefinition parent = null;
                    Keyword current = call.getParent();

                    while(current != null){
                        if(current.getClass() == Step.class) {
                            current = ((Step) current).getParent();
                        }
                        else if(current.getClass() == KeywordDefinition.class){
                            parent = (KeywordDefinition) current;
                            break;
                        }
                        else{
                            throw new RuntimeException(String.format("Failed to resolve dynamic import in file %s, lines %s",
                                    call.getFileName(),
                                    call.getLineRange().toString()));
                        }
                    }

                    if(parent != null){
                        this.scope.setDynamicResources(parent, call.getParameters());
                    }
                }
            }
        );
    }

    public Set<Variable> findInScope(Set<TestCase> testCases, Set<String> suites, String name){
        return scope.findInScope(testCases, suites, name);
    }

    public ResourcesTable getDynamicResources(KeywordDefinition keyword){
        return scope.getDynamicResources(keyword);
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

    @Override
    public Keyword findKeyword(String library, String name) throws InstantiationException, IllegalAccessException {
        return libraries.findKeyword(library, name);
    }
}
