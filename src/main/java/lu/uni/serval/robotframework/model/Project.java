package lu.uni.serval.robotframework.model;

import java.io.File;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.*;

public class Project implements Comparable<Project> {
    private List<TestCaseFile> testCaseFiles;
    private Map<File, TestCaseFile> files;
    private LibraryResources libraries;
    private String gitUrl;
    private String commitId;
    private LocalDateTime dateTime;

    public Project(){
        testCaseFiles = new ArrayList<>();
        files = new HashMap<>();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public boolean hasFile(File file){
        return files.containsKey(file);
    }

    public List<TestCaseFile> getTestCaseFiles(){
        return testCaseFiles;
    }

    public TestCaseFile getTestCaseFile(File file) {
        return files.get(file);
    }

    public TestCaseFile getFile(File file){
        return files.get(file);
    }

    public Map<File, TestCaseFile> getFiles(){
        return files;
    }

    public String getCommitId() {
        return commitId;
    }

    public String getGitUrl() {
        return gitUrl;
    }

    public LibraryResources getLibraries() {
        return libraries;
    }

    public Set<UserKeyword> getUserKeywords() {
        Set<UserKeyword> keywords = new HashSet<>();

        for(TestCaseFile testCaseFile: testCaseFiles){
            keywords.addAll(testCaseFile.getUserKeywords());
        }

        return keywords;
    }

    public void setLibraries(LibraryResources libraries) {
        this.libraries = libraries;
    }

    public void addFile(File file){
        if(hasFile(file)){
            return;
        }

        files.put(file, null);
    }

    public void addTestCaseFile(TestCaseFile testCaseFile){
        testCaseFiles.add(testCaseFile);

        files.put(testCaseFile.getFile(), testCaseFile);

        updateFiles(testCaseFile.getSettings());
    }

    private void updateFiles(Settings settings){
        for(Resources resources: settings.getResources()){
            addFile(resources.getFile());
        }
    }

    @Override
    public int compareTo(Project other) {
        if(dateTime == other.dateTime){
            return 0;
        }

        return dateTime.isBefore(other.dateTime) ? -1 : 1;
    }
}
