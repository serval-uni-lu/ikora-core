package org.ukwikora.model;

import org.ukwikora.runner.Runtime;

import java.io.File;
import java.util.*;

public class Suite {
    private String name;
    private String documentation;
    private File source;
    private List<Suite> suites;
    private TestCaseFile testCaseFile;

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

    public List<TestCaseFile> getTestCaseFiles() {
        List<TestCaseFile> files = new ArrayList<>();

        for(Suite suite: suites){
            files.addAll(getTestCaseFiles());
        }

        if(testCaseFile != null){
            files.add(testCaseFile);
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

        if(testCaseFile != null){
            testCases.addAll(testCaseFile.getTestCases());
        }

        return testCases;
    }

    public List<UserKeyword> getUserKeywords(){
        List<UserKeyword> keywords = new ArrayList<>();

        for(Suite suite: suites){
            keywords.addAll(suite.getUserKeywords());
        }

        if(testCaseFile != null){
            keywords.addAll(testCaseFile.getUserKeywords());
        }

        return keywords;
    }

    public Set<UserKeyword> findUserKeyword(String name) {
        Set<UserKeyword> userKeywordsFound = new HashSet<>();

        for(Suite suite: suites){
            userKeywordsFound.addAll(suite.findUserKeyword(name));
        }

        if(testCaseFile != null){
            userKeywordsFound.addAll(testCaseFile.findUserKeyword(name));
        }

        return userKeywordsFound;
    }

    public Set<UserKeyword> findUserKeyword(String library, String name) {
        Set<UserKeyword> userKeywordsFound = new HashSet<>();

        for(Suite suite: suites){
            userKeywordsFound.addAll(suite.findUserKeyword(library, name));
        }

        if(testCaseFile != null){
            userKeywordsFound.addAll(testCaseFile.findUserKeyword(library, name));
        }

        return userKeywordsFound;
    }

    public Set<Variable> getVariables() {
        Set<Variable> variables = new HashSet<>();

        for(Suite suite: suites){
            variables.addAll(suite.getVariables());
        }

        if(testCaseFile != null){
            variables.addAll(testCaseFile.getVariables());
        }

        return variables;
    }

    public Set<Resources> getSettings() {
        Set<Resources> externalResources = new HashSet<>();

        for(Suite suite: suites){
            externalResources.addAll(suite.getSettings());
        }

        if(testCaseFile != null){
            externalResources.addAll(testCaseFile.getSettings().getExternalResources());
        }

        return externalResources;
    }

    public int getDeadLoc() {
        int deadLoc = 0;

        for(Suite suite: suites){
            deadLoc += suite.getDeadLoc();
        }

        if(testCaseFile != null){
            deadLoc += testCaseFile.getDeadLoc();
        }

        return deadLoc;
    }

    public void execute(Runtime runtime) throws Exception {
        runtime.enterSuite(this);

        for(Suite suite: suites){
            suite.execute(runtime);
        }

        if(testCaseFile != null){
            for(TestCase testCase: testCaseFile.getTestCases()){
                testCase.execute(runtime);
            }
        }

        runtime.exitSuite(this);
    }

    void addTestCaseFile(TestCaseFile testCaseFile) {
        if(testCaseFile.getFile().equals(this.getSource())){
            this.testCaseFile = testCaseFile;
        }
        else{
            addSuite(SuiteFactory.create(this, testCaseFile));
        }
    }
}
