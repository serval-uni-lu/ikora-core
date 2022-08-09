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

import lu.uni.serval.ikora.core.model.LibraryKeyword;
import lu.uni.serval.ikora.core.model.TestCase;
import lu.uni.serval.ikora.core.model.UserKeyword;

public class ConnectivityVisitor extends DependencyVisitor {
    private int connectivity;

    public ConnectivityVisitor() {
        this.connectivity = 0;
    }

    public int getConnectivity(){
        return connectivity;
    }

    @Override
    public void visit(TestCase testCase, VisitorMemory memory) {
        // connectivity is always 0
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        connectivity += keyword.getDependencies().isEmpty() ? 0 : 1;
        VisitorUtils.traverseDependencies(this, keyword, memory);
    }

    @Override
    public void visit(LibraryKeyword keyword, VisitorMemory memory) {
        connectivity += keyword.getDependencies().isEmpty() ? 0 : 1;
        VisitorUtils.traverseDependencies(this, keyword, memory);
    }
}
