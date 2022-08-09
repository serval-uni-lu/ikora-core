/*
 *
 *     Copyright © 2019 - 2022 University of Luxembourg
 *
 *     Licensed under the Apache License, Version 2.0 (the "License")
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package lu.uni.serval.ikora.core.model;

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

    public List<Suite> getSuites() {
        return suites;
    }

    public boolean isSourceFile() {
        return sourceFile != null;
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
