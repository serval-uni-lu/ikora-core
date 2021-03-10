package lu.uni.serval.ikora.core.model;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

public class Project implements Comparable<Project> {
    private final List<Suite> suites;
    private final Map<String, SourceFile> files;
    private final Set<Project> dependencies;
    private final Source rootFolder;

    private Instant date;
    private int loc;

    public Project(Source source){
        rootFolder = source;
        suites = new ArrayList<>();
        files = new HashMap<>();
        dependencies = new HashSet<>();
        loc = 0;
    }

    public void setDate(Instant date){
        this.date = date;
    }

    public String getName() {
        return rootFolder.getName();
    }

    public Source getRootFolder() {
        return rootFolder;
    }

    public List<Suite> getSuites(){
        return suites;
    }

    public Instant getDate() {
        return date;
    }

    public List<SourceFile> getSourceFiles(){
        return new ArrayList<>(files.values());
    }

    public Optional<SourceFile> getSourceFile(String name) {
        return Optional.ofNullable(files.get(name));
    }

    public Optional<SourceFile> getSourceFile(URI uri){
        String name = generateFileName(new Source(new File(uri.getPath())));
        return getSourceFile(name);
    }

    public Map<String, SourceFile> getFiles(){
        return files;
    }

    public Set<TestCase> getTestCases(){
        Set<TestCase> testCases = new HashSet<>();

        for(SourceFile file: files.values()){
            testCases.addAll(file.getTestCases());
        }

        return testCases;
    }

    public Set<UserKeyword> getUserKeywords(){
        Set<UserKeyword> userKeywords = new HashSet<>();

        for(SourceFile file: files.values()){
            userKeywords.addAll(file.getUserKeywords());
        }

        return userKeywords;
    }

    public Set<VariableAssignment> getVariableAssignments(){
        Set<VariableAssignment> variables = new HashSet<>();

        for(SourceFile file: files.values()){
            variables.addAll(file.getVariables());
        }

        return variables;
    }

    public Set<TestCase> findTestCase(String library, String name) {
        Set<TestCase> testCasesFound = new HashSet<>();

        for(SourceFile file: files.values()){
            testCasesFound.addAll(file.findTestCase(library, Token.fromString(name)));
        }

        return testCasesFound;
    }

    public Set<UserKeyword> findUserKeyword(Token name) {
        Set<UserKeyword> userKeywordsFound = new HashSet<>();

        for(SourceFile file: files.values()){
            userKeywordsFound.addAll(file.findUserKeyword(name));
        }

        return userKeywordsFound;
    }

    public Set<UserKeyword> findUserKeyword(String library, String name) {
        Set<UserKeyword> userKeywordsFound = new HashSet<>();

        for(SourceFile file: files.values()){
            userKeywordsFound.addAll(file.findUserKeyword(library, Token.fromString(name)));
        }

        return userKeywordsFound;
    }

    public Set<Resources> getExternalResources() {
        Set<Resources> externalResources = new HashSet<>();

        for(SourceFile file: files.values()){
            externalResources.addAll(file.getSettings().getExternalResources());
        }

        return externalResources;
    }

    public long getEpoch() {
        return this.getDate().toEpochMilli();
    }

    public int getLoc() {
        return loc;
    }

    public int getDeadLoc() {
        int deadLoc = 0;

        for(SourceFile file: files.values()){
            deadLoc += file.getDeadLoc();
        }

        return deadLoc;
    }

    public Set<Project> getDependencies() {
        return this.dependencies;
    }

    public boolean isDependency(Project project) {
        return this.dependencies.contains(project);
    }

    public void addFile(Source source){
        String key = generateFileName(source);

        if(files.containsKey(key)){
            return;
        }

        files.put(key, null);
    }

    public void addSourceFile(SourceFile sourceFile){
        if(sourceFile == null){
            return;
        }

        files.put(sourceFile.getName(), sourceFile);
        updateSuites(sourceFile);
        updateFiles(sourceFile.getSettings());

        this.loc += sourceFile.getLinesOfCode();
    }

    private void updateSuites(SourceFile sourceFile){
        if(sourceFile.getTestCases().isEmpty()){
            return;
        }

        if(sourceFile.getSource().isInMemory()){
            return;
        }

        String name = SuiteFactory.computeName(sourceFile, true);
        Optional<Suite> suite = suites.stream().filter(s -> s.getName().equals(name)).findAny();

        if(suite.isPresent()){
            suite.get().addSourceFile(sourceFile);
        }
        else {
            suites.add(SuiteFactory.create(sourceFile));
        }
    }

    private void updateFiles(Settings settings){
        for(Resources resources: settings.getInternalResources()){
            addFile(new Source(resources.getFile()));
        }
    }

    public void addDependency(Project dependency) {
        if(dependency != null){
            dependencies.add(dependency);
        }
    }

    public String generateFileName(Source source) {
        if(source.isInMemory()){
            return source.getName();
        }

        File file = this.getRootFolder().asFile();

        if(file.isFile()){
            file = file.getParentFile();
        }

        Path base = Paths.get(file.getAbsolutePath().trim());

        Path path = Paths.get(source.getAbsolutePath().trim()).normalize();

        return base.relativize(path).toString();
    }

    @Override
    public int compareTo(Project other) {
        return date.compareTo(other.date);
    }
}
