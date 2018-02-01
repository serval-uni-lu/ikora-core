package lu.uni.serval.robotframework.model;

import lu.uni.serval.utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KeywordTreeFactory {

    static public List<TreeNode<KeywordData>> create(TestCaseFile testCaseFile){
        List<TreeNode<KeywordData>> keywordForest = new ArrayList< TreeNode<KeywordData>>();

        for(TestCase testCase : testCaseFile) {
            KeywordData keywordData = createKeywordData(testCaseFile, testCase);
            TreeNode<KeywordData> root = new TreeNode<KeywordData>(keywordData);

            visitSteps(testCaseFile, root, testCase);

            keywordForest.add(root);
        }

        return keywordForest;
    }

    static private void visitSteps(TestCaseFile testCaseFile, TreeNode<KeywordData> current, TestCase testCase) {
        for(Step step : testCase) {
            UserKeyword keyword = testCaseFile.findUserKeyword(step);
            KeywordData keywordData = createKeywordData(testCaseFile, keyword);

            TreeNode<KeywordData> child =  current.addChild(keywordData);
            visitSteps(testCaseFile, child, keyword);
        }
    }

    static private void setArguments(TestCaseFile testCaseFile, UserKeyword keyword, KeywordData keywordData){
        for (Argument argument : keyword.getArguments()) {

            if(argument.hasVariable()) {
                Map<String, List<String>> values = testCaseFile.getVariableValues(argument);
                keywordData.variables.putAll(values);
            }

            keywordData.arguments.add(argument.toString());
        }
    }

    static private void setName(TestCaseFile testCaseFile, TestCase testCase, KeywordData keywordData) {
        Argument name = testCase.getName();

        if(name.hasVariable()) {
            Map<String, List<String>> values = testCaseFile.getVariableValues(name);
            keywordData.variables.putAll(values);
        }

        keywordData.name  = name.toString();
    }

    static private KeywordData createKeywordData(TestCaseFile testCaseFile, TestCase testCase) {
        KeywordData keywordData = new KeywordData();
        keywordData.file = testCase.getFile();
        keywordData.documentation = testCase.getDocumentation();
        setName(testCaseFile, testCase, keywordData);

        if(testCase instanceof UserKeyword) {
            setArguments(testCaseFile, (UserKeyword)testCase, keywordData);
        }

        return keywordData;
    }
}
