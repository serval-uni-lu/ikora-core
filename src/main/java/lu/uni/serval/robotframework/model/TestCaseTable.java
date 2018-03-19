package lu.uni.serval.robotframework.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestCaseTable implements Iterable<TestCase> {
    private List<TestCase> testCaseList;

    public TestCaseTable(){
        testCaseList = new ArrayList<TestCase>();
    }

    public List<TestCase> getTestCases() {
        return testCaseList;
    }

    public Iterator<TestCase> iterator() {
        return testCaseList.iterator();
    }

    public void add(TestCase userKeyword) {
        testCaseList.add(userKeyword);
    }
}
