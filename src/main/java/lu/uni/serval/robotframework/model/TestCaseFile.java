package lu.uni.serval.robotframework.model;

import javax.naming.directory.NoSuchAttributeException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TestCaseFile implements Iterable<TestCase> {
    private String directory;
    private String name;
    private Settings settings;
    private TestCaseTable testCaseTable;
    private KeywordTable keywordTable;
    private VariableTable variableTable;

    public TestCaseFile(String directory, String name, Settings settings, TestCaseTable testCaseTable,
                        KeywordTable keywordTable, VariableTable variableTable) {
        this.directory = directory;
        this.name = name;
        this.settings = settings;
        this.testCaseTable = testCaseTable;
        this.keywordTable = keywordTable;
        this.variableTable = variableTable;
    }

    public String getDirectory(){
        return this.directory;
    }

    public String getName() {
        return name;
    }

    public Settings getSettings() {
        return this.settings;
    }

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

    public List<String> getVariableValue(String name) throws NoSuchAttributeException {
        return getVariableValue(name, new LinkedList<TestCaseFile>());
    }

    protected List<String> getVariableValue(String name, Queue<TestCaseFile> fileQueue) throws NoSuchAttributeException {
        if (this.variableTable.containsKey(name)) {
            return this.variableTable.get(name);
        }

        for (Resources resources : this.settings.getResources()) {
            if(resources.getType() == Resources.Type.Resource) {
                fileQueue.add(resources.getFile());
            }
        }

        if(!fileQueue.isEmpty()) {
            TestCaseFile currentFile = fileQueue.poll();
            return currentFile.getVariableValue(name, fileQueue);
        }

        throw new NoSuchAttributeException();
    }
}
