/*
 *
 *     Copyright © 2019 - 2022 University of Luxembourg
 *
 *     Licensed under the Apache License, Version 2.0 (the "License")
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package lu.uni.serval.ikora.core.analytics.visitor;

import lu.uni.serval.ikora.core.model.KeywordDefinition;
import lu.uni.serval.ikora.core.model.TestCase;
import lu.uni.serval.ikora.core.model.UserKeyword;

import java.util.HashSet;
import java.util.Set;

public class DependencyCollectorVisitor extends DependencyVisitor {
    private final Set<KeywordDefinition> dependencies;

    public DependencyCollectorVisitor(){
        this.dependencies = new HashSet<>();
    }

    public Set<KeywordDefinition> getDependencies() {
        return dependencies;
    }

    @Override
    public void visit(TestCase testCase, VisitorMemory memory) {
        dependencies.add(testCase);
        VisitorUtils.traverseDependencies(this, testCase, memory);
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        dependencies.add(keyword);
        VisitorUtils.traverseDependencies(this, keyword, memory);
    }
}
