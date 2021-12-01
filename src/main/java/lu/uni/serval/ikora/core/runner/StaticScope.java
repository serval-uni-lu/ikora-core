package lu.uni.serval.ikora.core.runner;

/*-
 * #%L
 * Ikora Core
 * %%
 * Copyright (C) 2019 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import lu.uni.serval.ikora.core.analytics.KeywordStatistics;
import lu.uni.serval.ikora.core.builder.DynamicImports;
import lu.uni.serval.ikora.core.model.*;

import java.util.*;

public class StaticScope implements Scope{
    private final SourceNodeTable<Variable> global;
    private final Map<TestCase, SourceNodeTable<Variable>> test;
    private final Map<String, SourceNodeTable<Variable>> suite;

    private final Map<KeywordDefinition, ResourcesTable> dynamicLibrary;

    public StaticScope(){
        global = new SourceNodeTable<>();
        suite = new HashMap<>();
        test = new HashMap<>();
        dynamicLibrary = new HashMap<>();
    }

    @Override
    public Set<Node> findInScope(Set<TestCase> testCases, Set<String> suites, Token name){
        Set<Node> variablesFound = new HashSet<>();

        for(TestCase testCase: testCases){
            variablesFound.addAll(findTestVariable(testCase, name));
        }

        for(String suiteName: suites){
            variablesFound.addAll(findSuiteVariable(suiteName, name));
        }

        variablesFound.addAll(findGlobalVariable(name));

        return variablesFound;
    }

    @Override
    public void addToGlobal(Variable variable) {
        global.add(variable);
    }

    @Override
    public void addToSuite(String suite, Variable variable) {
        this.suite.putIfAbsent(suite, new SourceNodeTable<>());
        this.suite.get(suite).add(variable);
    }

    @Override
    public void addToKeyword(Keyword keyword, Variable variable) {

    }

    @Override
    public void addToTest(TestCase testCase, Variable variable) {
        test.putIfAbsent(testCase, new SourceNodeTable<>());
        test.get(testCase).add(variable);
    }

    @Override
    public void addDynamicLibrary(KeywordDefinition keyword, List<Argument> argumentList) {
        if(keyword == null){
            return;
        }

        if(argumentList.isEmpty()){
            return;
        }

        if(keyword.getSourceFile().getSource().isInMemory()){
            return;
        }

        dynamicLibrary.putIfAbsent(keyword, new ResourcesTable());

        final Token name = argumentList.get(0).getDefinitionToken();

        final Resources resources = new Resources(Token.empty(), name, DynamicImports.getImportArguments(argumentList));
        dynamicLibrary.get(keyword).add(resources);
    }

    @Override
    public ResourcesTable getDynamicResources(Node node) {
        final Set<KeywordDefinition> dependencies = KeywordStatistics.getDependencies(node);

        ResourcesTable resourcesTable = new ResourcesTable();

        for(KeywordDefinition dependency: dependencies){
            for(Resources resources: dynamicLibrary.get(dependency)){
                if(resources != null) {
                    resourcesTable.add(resources);
                }
            }
        }

        return resourcesTable;
    }

    @Override
    public void enterNode(Node node) {
        // No concept of entering or leaving when performing static analysis
    }

    @Override
    public void exitNode(Node node) {
        // No concept of entering or leaving when performing static analysis
    }

    @Override
    public void enterSuite(Suite suite) {
        // No concept of entering or leaving when performing static analysis
    }

    @Override
    public void exitSuite(Suite suite) {
        // No concept of entering or leaving when performing static analysis
    }

    @Override
    public void reset() {
        global.clear();
        suite.clear();
        test.clear();
        dynamicLibrary.clear();
    }

    @Override
    public TestCase getTestCase() {
        return null;
    }

    @Override
    public NodeList<Value> getReturnValues() {
        return new NodeList<>();
    }

    private Set<Variable> findTestVariable(TestCase testCase, Token name) {
        if(!test.containsKey(testCase)){
            return Collections.emptySet();
        }

        return test.get(testCase).findNode(name);
    }


    private Set<Variable> findSuiteVariable(String suite, Token name) {
        if(!this.suite.containsKey(suite)){
            return Collections.emptySet();
        }

        return this.suite.get(suite).findNode(name);
    }


    private Set<Variable> findGlobalVariable(Token name) {
        return global.findNode(name);
    }
}
