package lu.uni.serval.robotframework.model;

import lu.uni.serval.utils.*;
import lu.uni.serval.utils.exception.DuplicateNodeException;
import lu.uni.serval.utils.tree.TreeNode;

import java.util.*;

public class KeywordTreeFactory {

    private Project project;
    private Set<TestCase> processed;

    public KeywordTreeFactory(Project project) {
        this.project = project;
    }

    public Set<TreeNode> create() throws DuplicateNodeException {
        Set<TreeNode> keywordForest = new HashSet<>();
        this.processed = new HashSet<>();

        for(TestCaseFile testCaseFile: project.getTestCaseFiles()){
            readFile(testCaseFile, keywordForest);
        }

        return keywordForest;
    }

    void readFile(TestCaseFile testCaseFile, Set<TreeNode> keywordForest) throws DuplicateNodeException {
        ArrayList<TestCase> keywords = new ArrayList<>();
        keywords.addAll(testCaseFile.getTestCases());
        keywords.addAll(testCaseFile.getUserKeywords());

        for(TestCase keyword : keywords){
            if(processed.contains(keyword)){
                 continue;
            }

            processed.add(keyword);

            KeywordData keywordData = createKeywordData(testCaseFile, keyword, null, null);
            TreeNode root = new TreeNode(keywordData, true);

            visitSteps(testCaseFile, root, keyword, new HashMap<>());

            keywordForest.add(root);
        }
    }

    private void visitSteps(TestCaseFile testCaseFile, TreeNode current, TestCase testCase, HashMap<String, List<String>> locals) throws DuplicateNodeException {
        for(Step step : testCase) {
            UserKeyword keyword = testCaseFile.findUserKeyword(step);
            KeywordData keywordData = createKeywordData(testCaseFile, keyword, step, locals);

            TreeNode child =  current.addChild(keywordData);
            visitSteps(testCaseFile, child, keyword, (HashMap)locals.clone());
        }
    }

    private void setArguments(TestCaseFile testCaseFile, UserKeyword keyword, KeywordData keywordData, Step step, Map<String, List<String>> locals){
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

    private void setName(TestCaseFile testCaseFile, TestCase testCase, KeywordData keywordData) {
        Argument name = testCase.getName();

        if(name.hasVariable()) {
            Map<String, List<String>> values = testCaseFile.getVariableValues(name);
            keywordData.variables.putAll(values);
        }

        keywordData.name  = name.toString();
    }

    private KeywordData createKeywordData(TestCaseFile testCaseFile, TestCase testCase, Step step, Map<String, List<String>> locals) {
        KeywordData keywordData = new KeywordData();
        keywordData.file = testCase.getFile();
        keywordData.documentation = testCase.getDocumentation();
        setName(testCaseFile, testCase, keywordData);

        if(testCase instanceof UserKeyword) {
            setArguments(testCaseFile, (UserKeyword)testCase, keywordData, step, locals);
        }

        return keywordData;
    }
}
