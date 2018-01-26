package lu.uni.serval;

import lu.uni.serval.robotframework.model.TestCaseFile;
import lu.uni.serval.robotframework.model.TestCaseFileFactory;

public class RFTestGenerator {
    public static void main(String[] args) {
        String filePath = "C:\\Users\\renaud.rwemalika\\Documents\\PhD\\robotframework\\webdemo\\login_tests\\gherkin_login.robot";

        TestCaseFileFactory factory = new TestCaseFileFactory();
        TestCaseFile testCaseFile = factory.create(filePath);
        System.out.println(testCaseFile.toString());
    }
}
