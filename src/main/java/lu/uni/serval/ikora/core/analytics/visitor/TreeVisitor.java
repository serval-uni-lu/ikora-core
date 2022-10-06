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
import org.apache.commons.lang3.NotImplementedException;

public class TreeVisitor extends EmptyVisitor {
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
        VisitorUtils.traverseSteps(this, testCase, memory);
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        VisitorUtils.traverseSteps(this, keyword, memory);
    }

    @Override
    public void visit(Step step, VisitorMemory memory) {
        if(step instanceof KeywordCall call){
            VisitorUtils.traverseKeywordCall(this, call, memory);
        }
        else if(step instanceof Assignment assignment){
            VisitorUtils.traverseAssignmentCall(this, assignment, memory);
        }
        else if(step instanceof ForLoop forLoop){
            VisitorUtils.traverseForLoopSteps(this, forLoop, memory);
        }
        else{
            throw new NotImplementedException(String.format("Visitor does not support class %s", step.getClass()));
        }
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        VisitorUtils.traverseKeywordCall(this, call, memory);
    }

    @Override
    public void visit(Assignment assignment, VisitorMemory memory) {
        VisitorUtils.traverseAssignmentCall(this, assignment, memory);
    }

    @Override
    public void visit(ForLoop forLoop, VisitorMemory memory) {
        VisitorUtils.traverseForLoopSteps(this, forLoop, memory);
    }

    @Override
    public void visit(Argument argument, VisitorMemory memory) {
        VisitorUtils.traverseArgument(this, argument, memory);
    }

    @Override
    public <T extends SourceNode> void visit(SourceNodeTable<T> ts, VisitorMemory memory) {
        throw new NotImplementedException("Cannot use a tree visitor to traverse a node table yet");
    }

    @Override
    public void visit(VariableAssignment variableAssignment, VisitorMemory memory) {
        VisitorUtils.traverseValues(this, variableAssignment, memory);
    }

    @Override
    public void visit(TestProcessing testProcessing, VisitorMemory memory) {
        VisitorUtils.traverseTestProcessing(this, testProcessing, memory);
    }
}
