package lu.uni.serval.robotframework.model;

import lu.uni.serval.utils.*;
import lu.uni.serval.utils.tree.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeywordTreeFactory {

    private TestCaseFile testCaseFile;

    public KeywordTreeFactory(TestCaseFile testCaseFile) {
        this.testCaseFile = testCaseFile;
    }

    public List<TreeNode> create(){
        List<TreeNode> keywordForest = new ArrayList<TreeNode>();

        ArrayList<TestCase> keywords = new ArrayList<TestCase>();
        keywords.addAll(testCaseFile.getTestCases());
        keywords.addAll(testCaseFile.getUserKeywords());

        for(TestCase keyword : keywords){
            KeywordData keywordData = createKeywordData(keyword, null, null);
            TreeNode root = new TreeNode(keywordData);

            visitSteps(root, keyword, new HashMap<String, List<String>>());

            keywordForest.add(root);
        }

        return keywordForest;
    }

    private void visitSteps(TreeNode current, TestCase testCase, HashMap<String, List<String>> locals) {
        for(Step step : testCase) {
            UserKeyword keyword = testCaseFile.findUserKeyword(step);
            KeywordData keywordData = createKeywordData(keyword, step, locals);

            TreeNode child =  current.addChild(keywordData);
            visitSteps(child, keyword, (HashMap)locals.clone());
        }
    }

    private void setArguments(UserKeyword keyword, KeywordData keywordData, Step step, Map<String, List<String>> locals){
        for (Argument argument : keyword.getArguments()) {

            if(argument.hasVariable()) {
                Map<String, List<String>> values = testCaseFile.getVariableValues(argument);
                keywordData.variables.putAll(values);
            }

            keywordData.arguments.add(argument.toString());
        }

        if(locals != null) {
            if(step != null) {
                locals.putAll(step.fetchVariables(keyword));
            }

            keywordData.variables.putAll(locals);
        }
    }

    private void setName(TestCase testCase, KeywordData keywordData) {
        Argument name = testCase.getName();

        if(name.hasVariable()) {
            Map<String, List<String>> values = testCaseFile.getVariableValues(name);
            keywordData.variables.putAll(values);
        }

        keywordData.name  = name.toString();
    }

    private KeywordData createKeywordData(TestCase testCase, Step step, Map<String, List<String>> locals) {
        KeywordData keywordData = new KeywordData();
        keywordData.file = testCase.getFile();
        keywordData.documentation = testCase.getDocumentation();
        setName(testCase, keywordData);

        if(testCase instanceof UserKeyword) {
            setArguments((UserKeyword)testCase, keywordData, step, locals);
        }

        return keywordData;
    }
}
