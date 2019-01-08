package org.ukwikora.model;

import javax.annotation.Nonnull;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class Project implements Comparable<Project> {
    private List<TestCaseFile> testCaseFiles;
    private Map<String, TestCaseFile> files;
    private Set<Project> dependencies;

    private File rootFolder;
    private String gitUrl;
    private String commitId;
    private LocalDateTime dateTime;
    private int loc;

    public Project(String file){
        rootFolder = new File(file.trim());
        testCaseFiles = new ArrayList<>();
        files = new HashMap<>();
        dependencies = new HashSet<>();
        loc = 0;
    }

    void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }

    void setCommitId(String commitId) {
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

    public Map<String, TestCaseFile> getFiles(){
        return files;
    }

    public String getCommitId() {
        return commitId;
    }

    public String getGitUrl() {
        return gitUrl;
    }

    public <T extends Element> ElementTable<T> getElements(Class<T> type) {
        ElementTable<T> keywords = new ElementTable<>();

        for(TestCaseFile testCaseFile: testCaseFiles){
            keywords.extend(testCaseFile.getElements(type));
        }

        return keywords;
    }

    public Set<Resources> getExternalResources() {
        Set<Resources> externalResources = new HashSet<>();

        for(TestCaseFile testCaseFile: testCaseFiles){
            externalResources.addAll(testCaseFile.getSettings().getExternalResources());
        }

        return externalResources;
    }

    public long getEpoch() {
        ZoneId zoneId = ZoneId.systemDefault();
        return this.getDateTime().atZone(zoneId).toEpochSecond();
    }

    public int getLoc() {
        return loc;
    }

    public Set<Project> getDependencies() {
        return this.dependencies;
    }

    public boolean isDependency(Project project) {
        return this.dependencies.contains(project);
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

        this.loc += testCaseFile.getLoc();
    }

    private void updateFiles(Settings settings){
        for(Resources resources: settings.getResources()){
            addFile(resources.getFile());
        }
    }

    public void addDependency(Project dependency) {
        if(dependency != null){
            dependencies.add(dependency);
        }
    }

    public String generateFileName(File file) {
        Path base = Paths.get(this.getRootFolder().getAbsolutePath().trim());
        Path path = Paths.get(file.getAbsolutePath().trim()).normalize();

        return base.relativize(path).toString();
    }

    @Override
    public int compareTo(@Nonnull Project other) {
        if(dateTime == other.dateTime){
            return 0;
        }

        return dateTime.isBefore(other.dateTime) ? -1 : 1;
    }
}
