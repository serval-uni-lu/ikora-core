package lu.uni.serval.robotframework.model;

import lu.uni.serval.utils.*;

import java.util.ArrayList;
import java.util.List;

public class KeywordTreeFactory {

    static public List<TreeNode<KeywordData>> create(TestCaseFile testCaseFile){
        List<TreeNode<KeywordData>> keywordForest = new ArrayList< TreeNode<KeywordData>>();

        for(TestCase testCase : testCaseFile) {
            KeywordData keywordData = createKeywordData(testCase);
            TreeNode<KeywordData> root = new TreeNode<KeywordData>(keywordData);

            visitSteps(testCaseFile, root, testCase);

            keywordForest.add(root);
        }

        return keywordForest;
    }

    static private void visitSteps(TestCaseFile testCaseFile, TreeNode<KeywordData> current, TestCase testCase) {
        for(Step step : testCase) {
            UserKeyword keyword = testCaseFile.findUserKeyword(step);
            KeywordData keywordData = createKeywordData(keyword);
            keywordData.arguments = keyword.getArguments();

            TreeNode<KeywordData> child =  current.addChild(keywordData);
            visitSteps(testCaseFile, child, keyword);
        }
    }

    static private KeywordData createKeywordData(TestCase testCase) {
        KeywordData keywordData = new KeywordData();
        keywordData.file = testCase.getFile();
        keywordData.name = testCase.getName();
        keywordData.documentation = testCase.getDocumentation();

        return keywordData;
    }
}
