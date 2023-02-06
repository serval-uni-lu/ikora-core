/*
 *
 *     Copyright © 2022 University of Luxembourg
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
package lu.uni.serval.ikora.core.runner;

import lu.uni.serval.ikora.core.model.TestCase;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ExecutionFilter {
    private final Set<String> tags;
    private final Set<String> names;

    ExecutionFilter(Set<String> tags, Set<String> names){
        this.tags = tags.stream().map(String::toLowerCase).collect(Collectors.toSet());
        this.names = names.stream().map(String::toLowerCase).collect(Collectors.toSet());
    }

    public ExecutionFilter() {
        this.tags = Collections.emptySet();
        this.names = Collections.emptySet();
    }

    List<TestCase> filter(List<TestCase> testCases){
        return testCases.stream().filter(this::isTestValid).toList();
    }

    private boolean isTestValid(TestCase testCase){
        if(!names.isEmpty() && !names.contains(testCase.getName().toLowerCase())){
            return false;
        }

        if(tags.isEmpty()){
            return true;
        }

        return testCase.getTags().stream().anyMatch(tag -> tags.contains(tag.getName().toLowerCase()));
    }
}
