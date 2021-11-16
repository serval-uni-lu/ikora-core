package lu.uni.serval.ikora.core.runner;

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

import lu.uni.serval.ikora.core.model.Literal;
import lu.uni.serval.ikora.core.model.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TestFilter {
    private Set<Literal> tags;
    private Set<String> names;

    TestFilter(Set<Literal> tags, Set<String> names){
        this.tags = tags;
        this.names = names;
    }

    List<TestCase> filter(List<TestCase> testCases){
        List<TestCase> filtered = new ArrayList<>(testCases.size());

        for(TestCase testCase: testCases){
            if(!tags.isEmpty() && !tags.retainAll(testCase.getTags())){
                continue;
            }

            if(!names.isEmpty() && !names.contains(testCase.getName().toLowerCase())){
                continue;
            }

            filtered.add(testCase);
        }

        return filtered;
    }
}
