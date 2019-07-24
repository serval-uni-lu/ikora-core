package org.ukwikora.runner;

import org.ukwikora.model.TestCase;

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

    Set<TestCase> filter(List<TestCase> testCases){
        Set<TestCase> filtered = new HashSet<>();

        for(TestCase testCase: testCases){
            filtered.add(testCase);
        }

        return filtered;
    }
}
