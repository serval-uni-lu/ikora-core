package lu.uni.serval.robotframework.model;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.*;

public class TestCaseFile implements Iterable<UserKeyword> {
    private File file;
    private Settings settings;
    private TestCaseTable testCaseTable;
    private KeywordTable keywordTable;
    private VariableTable variableTable;
    private Map<String, List<String>> variableDictionary;

    public void setFile(File file) {
        this.file = file;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public void setTestCaseTable(TestCaseTable testCaseTable) {
        this.testCaseTable = testCaseTable;
    }

    public void setKeywordTable(KeywordTable keywordTable) {
        this.keywordTable = keywordTable;
    }

    public void setVariableTable(VariableTable variableTable) {
        this.variableTable = variableTable;
    }

    public File getFile() {
        return file;
    }

    public String getDirectory(){
        return this.file.getParent();
    }

    public String getPath() {
        return this.file.getPath();
    }

    public String getName() {
        return this.getName();
    }

    public Settings getSettings() {
        return this.settings;
    }

    public List<UserKeyword> getTestCases(){
        return testCaseTable.getTestCases();
    }

    public List<UserKeyword> getUserKeywords() {
        List<UserKeyword> userKeywords = new ArrayList<>(keywordTable.getUserKeywords());

        for(Resources resources: settings.getResources()){
            resources.getTestCaseFile().getUserKeywords(userKeywords);
        }

        return userKeywords;
    }

    private void getUserKeywords(List<UserKeyword> parentUserKeywords){
        List<UserKeyword> userKeywords = getUserKeywords();

        for(UserKeyword userKeyword : userKeywords){
            if(!parentUserKeywords.contains(userKeyword)){
                parentUserKeywords.add(userKeyword);
            }
        }
    }

    private Map<String, List<String>> getVariableDictionary() {
        if (this.variableDictionary != null) {
            return this.variableDictionary;
        }

        this.variableDictionary = new HashMap<>();
        buildVariableDictionary(this.variableDictionary, new LinkedList<>());

        return this.variableDictionary;
    }

    @Nonnull
    public Iterator<UserKeyword> iterator() {
        return testCaseTable.iterator();
    }

    public UserKeyword findUserKeyword(Step step) {
        for(UserKeyword userKeyword : keywordTable) {
            if (userKeyword.isEqual(step)) {
                return userKeyword;
            }
        }

        for (Resources resources : settings.getResources()) {
            UserKeyword userKeyword = resources.getTestCaseFile().findUserKeyword(step);

            if(userKeyword != null) {
                return userKeyword;
            }
        }

        UserKeyword keyword = new UserKeyword();


        return null;
    }

    public Map<String, List<String>> getVariableValues(Argument argument) {
        Map<String, List<String>> returnValues = new HashMap<>();

        List<String> variables = argument.findVariables();
        updateValues(returnValues, variables);

        return returnValues;
    }

    private void updateValues(Map<String, List<String>> returnValues, List<String> variables) {
        Map<String, List<String>> references = getVariableDictionary();

        for(String variable: variables) {
            if(returnValues.containsKey(variable)) {
                continue;
            }

            if(!references.containsKey(variable)) {
                continue;
            }

            List<String> currentValues = references.get(variable);
            List<String> currentVariables = new ArrayList<>();

            for(String currentValue : currentValues) {
                currentVariables.addAll(Argument.findVariables(currentValue));
            }

            returnValues.put(variable, currentValues);
            updateValues(returnValues, currentVariables);
        }
    }

    private void buildVariableDictionary(Map<String, List<String>> variables, Queue<TestCaseFile> fileQueue) {
        for (Map.Entry<String, List<String>> cursor : this.variableTable.entrySet()) {
            if(!variables.containsKey(cursor.getKey())) {
                variables.put(cursor.getKey(), cursor.getValue());
            }
        }

        for (Resources resources : this.settings.getResources()) {
                fileQueue.add(resources.getTestCaseFile());
        }

        if(!fileQueue.isEmpty()) {
            TestCaseFile nextFile = fileQueue.poll();
            nextFile.buildVariableDictionary(variables, fileQueue);
        }
    }
}
