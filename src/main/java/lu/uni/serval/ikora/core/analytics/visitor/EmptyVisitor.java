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

import lu.uni.serval.ikora.core.model.*;

public class EmptyVisitor implements NodeVisitor{
    @Override
    public void visit(SourceNode sourceNode, VisitorMemory memory) {
        VisitorUtils.accept(this, sourceNode, memory);
    }

    @Override
    public void visit(SourceFile sourceFile, VisitorMemory memory) {
        VisitorUtils.traverseSourceFile(this, sourceFile, memory);
    }

    @Override
    public void visit(TestCase testCase, VisitorMemory memory) {
        //nothing to do in this node
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        //nothing to do in this node
    }

    @Override
    public void visit(Step step, VisitorMemory memory) {
        //nothing to do in this node
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        //nothing to do in this node
    }

    @Override
    public void visit(Assignment assignment, VisitorMemory memory) {
        //nothing to do in this node
    }

    @Override
    public void visit(ForLoop forLoop, VisitorMemory memory) {
        //nothing to do in this node
    }

    @Override
    public void visit(LibraryKeyword keyword, VisitorMemory memory) {
        //nothing to do in this node
    }

    @Override
    public void visit(ScalarVariable scalar, VisitorMemory memory) {
        //nothing to do in this node
    }

    @Override
    public void visit(DictionaryVariable dictionary, VisitorMemory memory) {
        //nothing to do in this node
    }

    @Override
    public void visit(ListVariable list, VisitorMemory memory) {
        //nothing to do in this node
    }

    @Override
    public void visit(TimeOut timeOut, VisitorMemory memory) {
        //nothing to do in this node
    }

    @Override
    public void visit(Argument argument, VisitorMemory memory) {
        //nothing to do in this node
    }

    @Override
    public <T extends SourceNode> void visit(SourceNodeTable<T> ts, VisitorMemory memory) {
        //nothing to do in this node
    }

    @Override
    public void visit(VariableAssignment variableAssignment, VisitorMemory memory) {
        //nothing to do in this node
    }

    @Override
    public void visit(LibraryVariable variable, VisitorMemory memory) {
        //nothing to do in this node
    }

    @Override
    public void visit(TestProcessing testProcessing, VisitorMemory memory) {
        //nothing to do in this node
    }

    @Override
    public void visit(Literal literal, VisitorMemory memory) {
        //nothing to do in this node
    }
}
