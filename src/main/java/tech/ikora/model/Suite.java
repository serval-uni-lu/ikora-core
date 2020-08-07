package tech.ikora.model;

import tech.ikora.runner.Runtime;

import java.io.File;
import java.util.*;

public class Suite {
    private final String name;
    private final String documentation;
    private final File source;
    private final List<Suite> suites;

    private SourceFile sourceFile;

    Suite(String name, File source){
        this.name = name;
        this.source = source;
        this.suites = new ArrayList<>();
        this.documentation = "";
    }

    public String getName() {
        return name;
    }

    public File getSource() {
        return source;
    }

    public String getDocumentation() {
        return documentation;
    }

    public List<SourceFile> getSourceFiles() {
        List<SourceFile> files = new ArrayList<>();

        for(Suite suite: suites){
            files.addAll(suite.getSourceFiles());
        }

        if(sourceFile != null){
            files.add(sourceFile);
        }

        return files;
    }

    public void addSuite(Suite suite){
        this.suites.add(suite);
    }

    public List<TestCase> getTestCases() {
        List<TestCase> testCases = new ArrayList<>();

        for(Suite suite: suites){
            testCases.addAll(suite.getTestCases());
        }

        if(sourceFile != null){
            testCases.addAll(sourceFile.getTestCases());
        }

        return testCases;
    }

    public List<UserKeyword> getUserKeywords(){
        List<UserKeyword> keywords = new ArrayList<>();

        for(Suite suite: suites){
            keywords.addAll(suite.getUserKeywords());
        }

        if(sourceFile != null){
            keywords.addAll(sourceFile.getUserKeywords());
        }

        return keywords;
    }

    public Set<UserKeyword> findUserKeyword(Token name) {
        Set<UserKeyword> userKeywordsFound = new HashSet<>();

        for(Suite suite: suites){
            userKeywordsFound.addAll(suite.findUserKeyword(name));
        }

        if(sourceFile != null){
            userKeywordsFound.addAll(sourceFile.findUserKeyword(name));
        }

        return userKeywordsFound;
    }

    public Set<UserKeyword> findUserKeyword(String library, Token name) {
        Set<UserKeyword> userKeywordsFound = new HashSet<>();

        for(Suite suite: suites){
            userKeywordsFound.addAll(suite.findUserKeyword(library, name));
        }

        if(sourceFile != null){
            userKeywordsFound.addAll(sourceFile.findUserKeyword(library, name));
        }

        return userKeywordsFound;
    }

    public Set<VariableAssignment> getVariables() {
        Set<VariableAssignment> variables = new HashSet<>();

        for(Suite suite: suites){
            variables.addAll(suite.getVariables());
        }

        if(sourceFile != null){
            variables.addAll(sourceFile.getVariables());
        }

        return variables;
    }

    public Set<Resources> getSettings() {
        Set<Resources> externalResources = new HashSet<>();

        for(Suite suite: suites){
            externalResources.addAll(suite.getSettings());
        }

        if(sourceFile != null){
            externalResources.addAll(sourceFile.getSettings().getExternalResources());
        }

        return externalResources;
    }

    public int getDeadLoc() {
        int deadLoc = 0;

        for(Suite suite: suites){
            deadLoc += suite.getDeadLoc();
        }

        if(sourceFile != null){
            deadLoc += sourceFile.getDeadLoc();
        }

        return deadLoc;
    }

    public void execute(Runtime runtime) throws Exception {
        runtime.enterSuite(this);

        for(Suite suite: suites){
            suite.execute(runtime);
        }

        if(sourceFile != null){
            for(TestCase testCase: sourceFile.getTestCases()){
                testCase.execute(runtime);
            }
        }

        runtime.exitSuite(this);
    }

    void addSourceFile(SourceFile sourceFile) {
        if(sourceFile.getSource().isInMemory()){
            return;
        }

        if(sourceFile.getSource().asFile().equals(this.getSource())){
            this.sourceFile = sourceFile;
        }
        else{
            addSuite(SuiteFactory.create(this, sourceFile));
        }
    }
}
