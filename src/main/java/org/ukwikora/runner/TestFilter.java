package org.ukwikora.runner;

import org.ukwikora.model.TestCase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestFilter {
    private Set<String> tags;
    private Set<String> names;

    TestFilter(Set<String> tags, Set<String> names){
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
