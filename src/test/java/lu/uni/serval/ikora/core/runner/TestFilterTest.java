package lu.uni.serval.ikora.core.runner;

import lu.uni.serval.ikora.core.model.Literal;
import lu.uni.serval.ikora.core.model.NodeList;
import lu.uni.serval.ikora.core.model.TestCase;
import lu.uni.serval.ikora.core.model.Token;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestFilterTest {
    @Test
    void testNoTestCaseNoTags(){
        final TestFilter filter = new TestFilter(Collections.emptySet(), Collections.emptySet());
        final List<TestCase> filtered = filter.filter(Collections.emptyList());
        assertEquals(0, filtered.size());
    }

    @Test
    void testTestCaseNoTags(){
        TestCase testCase = new TestCase(Token.fromString("Test case 1"));
        final TestFilter filter = new TestFilter(Collections.emptySet(), Collections.emptySet());
        final List<TestCase> filtered = filter.filter(Collections.singletonList(testCase));
        assertEquals(1, filtered.size());
    }

    @Test
    void testTestCaseNoMatchingTags(){
        TestCase testCase = new TestCase(Token.fromString("Test case 1"));
        final TestFilter filter = new TestFilter(Collections.emptySet(), Collections.singleton("Tag 1"));
        final List<TestCase> filtered = filter.filter(Collections.singletonList(testCase));
        assertEquals(0, filtered.size());
    }

    @Test
    void testTestCaseMatchingTag(){
        NodeList<Literal> tags = new NodeList<>();
        tags.add(new Literal(Token.fromString("Tag 1")));
        tags.add(new Literal(Token.fromString("Tag 2")));
        TestCase testCase = new TestCase(Token.fromString("Test case 1"));
        testCase.setTags(tags);
        final TestFilter filter = new TestFilter(Collections.singleton("Tag 1"), Collections.emptySet());
        final List<TestCase> filtered = filter.filter(Collections.singletonList(testCase));
        assertEquals(1, filtered.size());
    }

    @Test
    void testTestCaseNoMatchingTag(){
        NodeList<Literal> tags = new NodeList<>();
        tags.add(new Literal(Token.fromString("Tag 1")));
        tags.add(new Literal(Token.fromString("Tag 2")));
        TestCase testCase = new TestCase(Token.fromString("Test case 1"));
        testCase.setTags(tags);
        final TestFilter filter = new TestFilter(Collections.singleton("Tag 3"), Collections.emptySet());
        final List<TestCase> filtered = filter.filter(Collections.singletonList(testCase));
        assertEquals(0, filtered.size());
    }

    @Test
    void testTestCaseNoMatchingName(){
        NodeList<Literal> tags = new NodeList<>();
        tags.add(new Literal(Token.fromString("Tag 1")));
        tags.add(new Literal(Token.fromString("Tag 2")));
        TestCase testCase = new TestCase(Token.fromString("Test case 1"));
        testCase.setTags(tags);
        final TestFilter filter = new TestFilter(Collections.singleton("Tag 1"), Collections.singleton("Other name"));
        final List<TestCase> filtered = filter.filter(Collections.singletonList(testCase));
        assertEquals(0, filtered.size());
    }

    @Test
    void testTestCaseMatchingName(){
        NodeList<Literal> tags = new NodeList<>();
        tags.add(new Literal(Token.fromString("Tag 1")));
        tags.add(new Literal(Token.fromString("Tag 2")));
        TestCase testCase = new TestCase(Token.fromString("Test case 1"));
        testCase.setTags(tags);
        final TestFilter filter = new TestFilter(Collections.singleton("Tag 1"), Collections.singleton("Test case 1"));
        final List<TestCase> filtered = filter.filter(Collections.singletonList(testCase));
        assertEquals(1, filtered.size());
    }
}