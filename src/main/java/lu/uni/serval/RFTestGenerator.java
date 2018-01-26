package lu.uni.serval;

import lu.uni.serval.robotframework.model.KeywordTreeFactory;
import lu.uni.serval.robotframework.model.TestCaseFile;
import lu.uni.serval.robotframework.model.TestCaseFileFactory;
import lu.uni.serval.utils.KeywordData;
import lu.uni.serval.utils.TreeNode;

import java.util.List;

public class RFTestGenerator {
    public static void main(String[] args) {
        String filePath = "C:\\Users\\renaud.rwemalika\\Documents\\PhD\\robotframework\\webdemo\\login_tests\\gherkin_login.robot";

        TestCaseFileFactory factory = new TestCaseFileFactory();
        TestCaseFile testCaseFile = factory.create(filePath);

        List<TreeNode<KeywordData>> forest = KeywordTreeFactory.create(testCaseFile);

        System.out.println(forest.toString());
    }
}
