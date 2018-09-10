package lu.uni.serval.robotframework.model;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

public class Project implements Comparable<Project> {
    private List<TestCaseFile> testCaseFiles;
    private Map<String, TestCaseFile> files;
    private LibraryResources libraries;
    private File rootFolder;
    private String gitUrl;
    private String commitId;
    private LocalDateTime dateTime;

    public Project(String file){
        rootFolder = new File(file.trim());
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

    public File getRootFolder() {
        return rootFolder;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public List<TestCaseFile> getTestCaseFiles(){
        return testCaseFiles;
    }

    public TestCaseFile getTestCaseFile(String name) {
        return files.get(name);
    }

    public TestCaseFile getFile(File file){
        return files.get(file);
    }

    public Map<String, TestCaseFile> getFiles(){
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

    public <T extends Element> ElementTable<T> getElements(Class<T> type) {
        ElementTable<T> keywords = new ElementTable<>();

        for(TestCaseFile testCaseFile: testCaseFiles){
            keywords.extend(testCaseFile.getElements(type));
        }

        return keywords;
    }

    public void setLibraries(LibraryResources libraries) {
        this.libraries = libraries;
    }

    public void addFile(File file){
        String key = generateFileName(file);

        if(files.containsKey(key)){
            return;
        }

        files.put(key, null);
    }

    public void addTestCaseFile(TestCaseFile testCaseFile){
        testCaseFiles.add(testCaseFile);

        String key = generateFileName(testCaseFile.getFile());
        files.put(key, testCaseFile);

        updateFiles(testCaseFile.getSettings());
    }

    private void updateFiles(Settings settings){
        for(Resources resources: settings.getResources()){
            addFile(resources.getFile());
        }
    }

    public String generateFileName(File file) {
        Path base = Paths.get(this.getRootFolder().getAbsolutePath().trim());
        Path path = Paths.get(file.getAbsolutePath().trim()).normalize();

        return base.relativize(path).toString();
    }

    @Override
    public int compareTo(Project other) {
        if(dateTime == other.dateTime){
            return 0;
        }

        return dateTime.isBefore(other.dateTime) ? -1 : 1;
    }
}
