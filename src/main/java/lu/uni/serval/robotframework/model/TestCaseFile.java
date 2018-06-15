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
}
