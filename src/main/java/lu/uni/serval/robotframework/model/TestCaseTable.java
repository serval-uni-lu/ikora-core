package lu.uni.serval.robotframework.model;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestCaseTable implements Iterable<UserKeyword> {
    private List<UserKeyword> testCaseList;

    public TestCaseTable(){
        testCaseList = new ArrayList<>();
    }

    public List<UserKeyword> getTestCases() {
        return testCaseList;
    }

    @Nonnull
    public Iterator<UserKeyword> iterator() {
        return testCaseList.iterator();
    }

    public void add(UserKeyword userKeyword) {
        testCaseList.add(userKeyword);
    }
}
