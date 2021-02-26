package lu.uni.serval.ikora.core.runner;

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
