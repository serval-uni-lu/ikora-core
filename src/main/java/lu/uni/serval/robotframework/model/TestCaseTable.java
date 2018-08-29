package lu.uni.serval.robotframework.model;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestCaseTable implements Iterable<TestCase> {
    private List<TestCase> testCaseList;
    private String file;

    public TestCaseTable(){
        testCaseList = new ArrayList<>();
    }

    public void setFile(String file){
        this.file = file;
    }

    public List<TestCase> getTestCases() {
        return testCaseList;
    }

    public String getFile(){
        return this.file;
    }

    @Nonnull
    public Iterator<TestCase> iterator() {
        return testCaseList.iterator();
    }

    public void add(TestCase testCase) {
        testCaseList.add(testCase);
    }

    public TestCase getTestCase(String name) {
        for(TestCase testCase: testCaseList){
            if(testCase.getName().toString().equalsIgnoreCase(name)){
                return testCase;
            }
        }

        return null;
    }
}
