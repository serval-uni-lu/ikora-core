package lu.uni.serval.ikora.core.analytics.visitor;

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

import lu.uni.serval.ikora.core.model.*;

public class DependencyVisitor implements NodeVisitor {
    @Override
    public void visit(SourceNode sourceNode, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, sourceNode, memory);
    }

    @Override
    public void visit(SourceFile sourceFile, VisitorMemory memory) {
        VisitorUtils.traverseSourceFileDependencies(this, sourceFile, memory);
    }

    @Override
    public void visit(TestCase testCase, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, testCase, memory);
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, keyword, memory);
    }

    @Override
    public void visit(Step step, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, step, memory);
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, call, memory);
    }

    @Override
    public void visit(Assignment assignment, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, assignment, memory);
    }

    @Override
    public void visit(ForLoop forLoop, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, forLoop, memory);
    }

    @Override
    public void visit(LibraryKeyword keyword, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, keyword, memory);
    }

    @Override
    public void visit(ScalarVariable scalar, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, scalar, memory);
    }

    @Override
    public void visit(DictionaryVariable dictionary, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, dictionary, memory);
    }

    @Override
    public void visit(ListVariable list, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, list, memory);
    }

    @Override
    public void visit(TimeOut timeOut, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, timeOut, memory);
    }

    @Override
    public void visit(Argument argument, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, argument, memory);
    }

    @Override
    public <T extends SourceNode> void visit(SourceNodeTable<T> nodeTable, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, nodeTable, memory);
    }

    @Override
    public void visit(VariableAssignment variableAssignment, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, variableAssignment, memory);
    }

    @Override
    public void visit(LibraryVariable libraryVariable, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, libraryVariable, memory);
    }

    @Override
    public void visit(TestProcessing testProcessing, VisitorMemory memory) {
        VisitorUtils.traverseDependencies(this, testProcessing, memory);
    }
}
