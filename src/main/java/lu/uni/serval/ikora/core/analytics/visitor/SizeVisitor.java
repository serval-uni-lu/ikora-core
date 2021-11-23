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

public class SizeVisitor extends EmptyVisitor {
    public static class Result{
        private int totalSize = 0;
        private int testCaseSize = 0;
        private int userKeywordSize = 0;
        private int libraryKeywordSize = 0;

        public int getTotalSize() {
            return totalSize;
        }

        public int getTestCaseSize() {
            return testCaseSize;
        }

        public int getUserKeywordSize() {
            return userKeywordSize;
        }

        public int getLibraryKeywordSize() {
            return libraryKeywordSize;
        }
    }

    private final Result result = new Result();

    public Result getResult() {
        return result;
    }

    @Override
    public void visit(TestCase testCase, VisitorMemory memory) {
        ++result.totalSize;
        ++result.testCaseSize;
        VisitorUtils.traverseSteps(this, testCase, memory);
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        ++result.totalSize;
        ++result.userKeywordSize;
        VisitorUtils.traverseSteps(this, keyword, memory);
    }

    @Override
    public void visit(LibraryKeyword keyword, VisitorMemory memory) {
        ++result.totalSize;
        ++result.libraryKeywordSize;
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
}
