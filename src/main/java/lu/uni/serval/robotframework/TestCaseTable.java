package lu.uni.serval.robotframework;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestCaseTable implements Iterable<TestCase> {
    private final List<TestCase> testCaseList = new ArrayList<TestCase>();

    public Iterator<TestCase> iterator() {
        return testCaseList.iterator();
    }

    public void add(TestCase userKeyword) {
        testCaseList.add(userKeyword);
    }
}
