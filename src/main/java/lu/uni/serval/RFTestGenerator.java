package lu.uni.serval;

import lu.uni.serval.robotframework.TestCaseFile;
import lu.uni.serval.robotframework.TestCaseFileFactory;
import lu.uni.serval.utils.TreeNode;

public class RFTestGenerator {
    public static void main(String[] args) {
        TestCaseFileFactory factory = new TestCaseFileFactory();
        TestCaseFile testCaseFile = factory.create("C:\\Users\\renaud.rwemalika\\Documents\\PhD\\robotframework\\webdemo\\login_tests\\gherkin_login.robot");
        System.out.println(testCaseFile.toString());
    }
}
