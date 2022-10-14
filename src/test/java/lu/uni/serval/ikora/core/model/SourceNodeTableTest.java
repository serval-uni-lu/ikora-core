package lu.uni.serval.ikora.core.model;

import lu.uni.serval.ikora.core.analytics.difference.Edit;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SourceNodeTableTest {
    @Test
    void testDifferenceEmptySameName(){
        final SourceNodeTable<TestCase> testCases1 = new SourceNodeTable<>();
        testCases1.setHeader(Token.fromString("Test Cases"));

        final SourceNodeTable<TestCase> testCases2 = new SourceNodeTable<>();
        testCases2.setHeader(Token.fromString("Test Cases"));

        final List<Edit> differences = testCases1.differences(testCases2);

        assertTrue(differences.isEmpty());
    }

//    @Test
//    void testDifferenceDifferentTypes(){
//        final SourceNodeTable<TestCase> testCases1 = new SourceNodeTable<>();
//        testCases1.setHeader(Token.fromString("Test Cases"));
//
//        final SourceNodeTable<UserKeyword> testCases2 = new SourceNodeTable<>();
//        testCases2.setHeader(Token.fromString("Test Cases"));
//
//        final List<Edit> differences = testCases1.differences(testCases2);
//
//        assertTrue(differences.isEmpty());
//    }
}