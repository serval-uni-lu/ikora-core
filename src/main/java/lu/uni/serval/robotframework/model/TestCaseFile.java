package lu.uni.serval.robotframework.model;

import javax.annotation.Nonnull;
import java.util.*;

public class TestCaseFile implements Iterable<TestCase> {
    private String directory;
    private String path;
    private String name;
    private Settings settings;
    private TestCaseTable testCaseTable;
    private KeywordTable keywordTable;
    private VariableTable variableTable;
    private Map<String, List<String>> variableDictionary;

    public TestCaseFile(String directory, String path, String name, Settings settings, TestCaseTable testCaseTable,
                        KeywordTable keywordTable, VariableTable variableTable) {
        this.directory = directory;
        this.path = path;
        this.name = name;
        this.settings = settings;
        this.testCaseTable = testCaseTable;
        this.keywordTable = keywordTable;
        this.variableTable = variableTable;
        this.variableDictionary = null;
    }

    public String getDirectory(){
        return this.directory;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public Settings getSettings() {
        return this.settings;
    }

    public List<TestCase> getTestCases(){
        return testCaseTable.getTestCases();
    }

    public List<UserKeyword> getUserKeywords() {
        List<UserKeyword> userKeywords = new ArrayList<UserKeyword>(keywordTable.getUserKeywords());

        for(Resources resources: settings.getResources()){
            if(resources.getType() == Resources.Type.Resource){
                resources.getFile().getUserKeywords(userKeywords);
            }
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

        this.variableDictionary = new HashMap<String, List<String>>();
        buildVariableDictionary(this.variableDictionary, new LinkedList<TestCaseFile>());

        return this.variableDictionary;
    }

    @Nonnull
    public Iterator<TestCase> iterator() {
        return testCaseTable.iterator();
    }

    public UserKeyword findUserKeyword(Step step) {
        for(UserKeyword userKeyword : keywordTable) {
            if (userKeyword.isEqual(step)) {
                return userKeyword;
            }
        }

        for (Resources resources : settings.getResources()) {
            if(resources.getType() != Resources.Type.Resource) {
                continue;
            }

            UserKeyword userKeyword = resources.getFile().findUserKeyword(step);

            if(userKeyword != null) {
                return userKeyword;
            }
        }

        return new UserKeyword(step);
    }

    public Map<String, List<String>> getVariableValues(Argument argument) {
        Map<String, List<String>> returnValues = new HashMap<String, List<String>>();

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
            List<String> currentVariables = new ArrayList<String>();

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
            if (resources.getType() == Resources.Type.Resource) {
                fileQueue.add(resources.getFile());
            }
        }

        if(!fileQueue.isEmpty()) {
            TestCaseFile nextFile = fileQueue.poll();
            nextFile.buildVariableDictionary(variables, fileQueue);
        }
    }
}
