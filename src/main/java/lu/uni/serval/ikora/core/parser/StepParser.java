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
package lu.uni.serval.ikora.core.parser;

import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.model.*;

import java.io.IOException;
import java.util.Iterator;

class StepParser {
    private StepParser() {}

    public static Step parse(int indent, LineReader reader, Token first, Iterator<Token> tokenIterator, boolean allowGherkin, ErrorManager errors) throws IOException {
        Step step;

        if(first.isForLoop()) {
            step = ForLoopParser.parse(indent, reader, first, tokenIterator, errors);
        }
        else if (first.isAssignment() || first.isVariable()){
            step = AssignmentParser.parse(reader, first, tokenIterator, errors);
        }
        else {
            step = KeywordCallParser.parse(reader, first, tokenIterator, allowGherkin, errors);
        }

        if(step == null){
            step = new InvalidStep(first);
        }

        return step;
    }
}
