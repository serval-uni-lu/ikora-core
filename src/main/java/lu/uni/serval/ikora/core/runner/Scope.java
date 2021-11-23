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

import lu.uni.serval.ikora.core.exception.RunnerException;
import lu.uni.serval.ikora.core.model.*;

import java.util.List;
import java.util.Set;

public interface Scope {
    void addToKeyword(Keyword keyword, Variable variable);
    void addToTest(TestCase testCase, Variable variable);
    void addToSuite(String suite, Variable variable);
    void addToGlobal(Variable variable);

    Set<Node> findInScope(Set<TestCase> testCases, Set<String> suites, Token name);
    
    void addDynamicLibrary(KeywordDefinition keyword, List<Argument> argumentList);
    ResourcesTable getDynamicResources(Node node);

    void enterNode(Node node) throws RunnerException;
    void exitNode(Node node);

    void enterSuite(Suite suite);
    void exitSuite(Suite suite);

    void reset();

    TestCase getTestCase();

    NodeList<Value> getReturnValues();
}
