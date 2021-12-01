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

public interface NodeVisitor {
    void visit(SourceNode sourceNode, VisitorMemory memory);
    void visit(SourceFile sourceFile, VisitorMemory memory);
    void visit(TestCase testCase, VisitorMemory memory);
    void visit(UserKeyword keyword, VisitorMemory memory);
    void visit(KeywordCall call, VisitorMemory memory);
    void visit(Assignment assignment, VisitorMemory memory);
    void visit(ForLoop forLoop, VisitorMemory memory);
    void visit(LibraryKeyword keyword, VisitorMemory memory);
    void visit(ScalarVariable scalar, VisitorMemory memory);
    void visit(DictionaryVariable dictionary, VisitorMemory memory);
    void visit(ListVariable list, VisitorMemory memory);
    void visit(TimeOut timeOut, VisitorMemory memory);
    void visit(Argument argument, VisitorMemory memory);
    void visit(Step step, VisitorMemory memory);
    void visit(VariableAssignment variableAssignment, VisitorMemory memory);
    void visit(LibraryVariable variable, VisitorMemory memory);
    void visit(TestProcessing testProcessing, VisitorMemory memory);
    void visit(Literal literal, VisitorMemory memory);
    <T extends SourceNode> void visit(SourceNodeTable<T> ts, VisitorMemory memory);
}
