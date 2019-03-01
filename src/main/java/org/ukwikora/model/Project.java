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

    public String getName() {
        return rootFolder.getName();
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

    public Set<TestCase> getTestCases(){
        Set<TestCase> testCases = new HashSet<>();

        for(TestCaseFile testCaseFile: testCaseFiles){
            testCases.addAll(testCaseFile.getTestCases());
        }

        return testCases;
    }

    public Set<UserKeyword> getUserKeywords(){
        Set<UserKeyword> userKeywords = new HashSet<>();

        for(TestCaseFile testCaseFile: testCaseFiles){
            userKeywords.addAll(testCaseFile.getUserKeywords());
        }

        return userKeywords;
    }

    public Optional<UserKeyword> findUserKeyword(String name) {
        for(TestCaseFile testCaseFile: testCaseFiles){
            UserKeyword userkeyword = testCaseFile.findUserKeyword(name);

            if(userkeyword != null){
                return Optional.of(userkeyword);
            }
        }

        return Optional.empty();
    }

    public Optional<UserKeyword> findUserKeyword(String library, String name) {
        for(TestCaseFile testCaseFile: testCaseFiles){
            UserKeyword userkeyword = testCaseFile.findUserKeyword(library, name);

            if(userkeyword != null){
                return Optional.of(userkeyword);
            }
        }

        return Optional.empty();
    }

    public Set<Variable> getVariables(){
        Set<Variable> variables = new HashSet<>();

        for(TestCaseFile testCaseFile: testCaseFiles){
            variables.addAll(testCaseFile.getVariables());
        }

        return variables;
    }

    public Set<Statement> getStatements(Class<? extends Statement> statementType) {
        if(statementType == TestCase.class){
            return new HashSet<>(getTestCases());
        }
        else if(statementType == UserKeyword.class) {
            return new HashSet<>(getUserKeywords());
        }
        else if(statementType == Variable.class) {
            return new HashSet<>(getVariables());
        }

        return null;
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
        File rootFolder = this.getRootFolder();

        if(rootFolder.isFile()){
            rootFolder = rootFolder.getParentFile();
        }

        Path base = Paths.get(rootFolder.getAbsolutePath().trim());

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
