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

public class SequenceVisitor extends EmptyVisitor {
    private final Sequence sequence;

    public SequenceVisitor(){
        sequence = new Sequence();
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
    public void visit(KeywordCall call, VisitorMemory memory) {
        call.getKeyword().ifPresent(keyword -> {
            if(LibraryKeyword.class.isAssignableFrom(keyword.getClass())){
                sequence.addStep(call);
            }
        });

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

    public Sequence getSequence(){
        return sequence;
    }

    public int getSequenceSize(){
        return sequence.size();
    }
}
