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

import lu.uni.serval.ikora.core.model.Assignment;
import lu.uni.serval.ikora.core.model.ForLoop;
import lu.uni.serval.ikora.core.model.KeywordCall;

public class StatementCounterVisitor extends TreeVisitor {
    private int statementCount;

    public StatementCounterVisitor(){
        statementCount = 0;
    }

    public int getStatementCount(){
        return statementCount;
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        ++statementCount;
        super.visit(call, memory);
    }

    @Override
    public void visit(Assignment assignment, VisitorMemory memory) {
        ++statementCount;
        super.visit(assignment, memory);
    }

    @Override
    public void visit(ForLoop forLoop, VisitorMemory memory) {
        ++statementCount;
        super.visit(forLoop, memory);
    }
}
