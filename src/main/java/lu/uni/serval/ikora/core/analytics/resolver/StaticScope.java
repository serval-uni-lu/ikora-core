package lu.uni.serval.ikora.core.analytics.resolver;

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
import lu.uni.serval.ikora.core.utils.Finder;

import java.util.*;

public class StaticScope implements ScopeManager {
    private final SourceNodeTable<Variable> global;
    private final Map<TestCase, SourceNodeTable<Variable>> test;
    private final Map<String, SourceNodeTable<Variable>> suite;

    private final LibraryResources libraryResources;
    private final Map<KeywordDefinition, ResourcesTable> dynamicLibrary;

    public StaticScope(LibraryResources libraryResources){
        this.libraryResources = libraryResources;
        global = new SourceNodeTable<>();
        suite = new HashMap<>();
        test = new HashMap<>();
        dynamicLibrary = new HashMap<>();
    }

    public Set<? super Keyword> findKeywords(KeywordCall call){
        return Finder.findKeywords(this.libraryResources, call);
    }

    public Optional<LibraryVariable> findLibraryVariable(Variable variable) {
        return Finder.findLibraryVariable(this.libraryResources, variable.getDefinitionToken());
    }

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
    public void addToGlobalScope(Variable variable) {
        global.add(variable);
    }

    @Override
    public void addToSuiteScope(String suite, Variable variable) {
        this.suite.putIfAbsent(suite, new SourceNodeTable<>());
        this.suite.get(suite).add(variable);
    }

    @Override
    public void addToTestScope(TestCase testCase, Variable variable) {
        test.putIfAbsent(testCase, new SourceNodeTable<>());
        test.get(testCase).add(variable);
    }

    @Override
    public void addLibraryToScope(KeywordDefinition keyword, List<Argument> argumentList) {
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

    public void reset() {
        global.clear();
        suite.clear();
        test.clear();
        dynamicLibrary.clear();
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
