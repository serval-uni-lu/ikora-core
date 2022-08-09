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

import lu.uni.serval.ikora.core.analytics.difference.Edit;
import lu.uni.serval.ikora.core.analytics.visitor.NodeVisitor;
import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.parser.Line;
import org.apache.commons.io.FilenameUtils;

import java.util.*;

public class SourceFile extends SourceNode {
    private final Project project;
    private final Source source;
    private final List<Line> lines;
    private final String name;

    private Settings settings;
    private SourceNodeTable<TestCase> testCaseTable;
    private SourceNodeTable<UserKeyword> userKeywordTable;
    private SourceNodeTable<VariableAssignment> variableTable;

    public SourceFile(Project project, Source source){
        this.project = project;
        this.source = source;
        this.lines = new ArrayList<>();
        this.name = this.project.generateFileName(this.source);

        setSettings(new Settings());
        setTestCaseTable(new SourceNodeTable<>());
        setKeywordTable(new SourceNodeTable<>());
        setVariableTable(new SourceNodeTable<>());
    }

    public List<Line> getLines(){
        return this.lines;
    }

    @Override
    public Tokens getTokens() {
        Tokens fileTokens = new Tokens();

        fileTokens.addAll(testCaseTable.getTokens());
        fileTokens.addAll(userKeywordTable.getTokens());
        fileTokens.addAll(variableTable.getTokens());

        return fileTokens;
    }

    @Override
    public Token getDefinitionToken() {
        return Token.fromString(this.name);
    }

    public int getDeadLoc() {
        int deadLoc = 0;

        for(UserKeyword keyword: userKeywordTable){
            if(keyword.getDependencies().isEmpty()){
                deadLoc += keyword.getLoc();
            }
        }

        for(VariableAssignment variable: variableTable){
            if(variable.getDependencies().isEmpty()){
                deadLoc += variable.getLoc();
            }
        }

        return deadLoc;
    }

    public int getLinesOfCode(){
        return getLinesOfCode(0, lines.size());
    }

    public int getLinesOfCode(int start, int end) {
        int loc = 0;

        for(int index = start; index < end; ++index){
            loc += lines.get(index).isCode() ? 1 : 0;
        }

        return loc;
    }

    @Override
    public SourceFile getSourceFile() {
        return this;
    }

    public void setSettings(Settings settings) {
        if(this.settings != null){
            removeAstChild(this.settings);
        }

        this.settings = settings;
        addAstChild(settings);
    }

    public void setTestCaseTable(SourceNodeTable<TestCase> testCaseTable) {
        if(this.testCaseTable != null){
            removeAstChild(this.testCaseTable);
        }

        this.testCaseTable = testCaseTable;
        addAstChild(testCaseTable);
    }

    public void setKeywordTable(SourceNodeTable<UserKeyword> userKeywordTable) {
        if(this.userKeywordTable != null){
            removeAstChild(this.userKeywordTable);
        }

        this.userKeywordTable = userKeywordTable;
        addAstChild(userKeywordTable);
    }

    public void setVariableTable(SourceNodeTable<VariableAssignment> variableTable) {
        if(this.variableTable != null){
            removeAstChild(this.variableTable);
        }

        this.variableTable = variableTable;
        addAstChild(variableTable);
    }

    @Override
    public Project getProject() {
        return project;
    }

    @Override
    public Source getSource() {
        return source;
    }

    public boolean isInMemory() {
        return this.source.isInMemory();
    }

    public String getDirectory(){
        return this.source.getDirectory();
    }

    public String getPath() {
        return this.source.getAbsolutePath();
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean matches(Token name) {
        return this.name.equalsIgnoreCase(name.getText());
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory) {
        visitor.visit(this, memory);
    }

    public Settings getSettings() {
        return this.settings;
    }

    public List<TestCase> getTestCases(){
        return testCaseTable.asList();
    }

    public List<UserKeyword> getUserKeywords() {
        return userKeywordTable.asList();
    }

    public List<VariableAssignment> getVariables() {
        return variableTable.asList();
    }

    @Override
    public long getEpoch() {
        return this.project.getEpoch();
    }

    Set<TestCase> getTestCase(Token name) {
        return testCaseTable.findNode(name);
    }

    public Iterator<UserKeyword> iterator() {
        return userKeywordTable.iterator();
    }

    public Set<TestCase> findTestCase(String library, Token name){
        return findNode(library, name, new HashSet<>(), TestCase.class);
    }

    public Set<UserKeyword> findUserKeyword(Token name) {
        return findNode(null, name, new HashSet<>(), UserKeyword.class);
    }

    public Set<UserKeyword> findUserKeyword(String library, Token name) {
        return findNode(library, name, new HashSet<>(), UserKeyword.class);
    }

    public Set<VariableAssignment> findVariable(Token name) {
        return findNode(null, name, new HashSet<>(), VariableAssignment.class);
    }

    public Set<VariableAssignment> findVariable(String library, Token name) {
        return findNode(library, name, new HashSet<>(), VariableAssignment.class);
    }

    public Set<Library> getAllLibraries() {
        final Set<Library> libraries = new HashSet<>();
        final Set<SourceFile> memory = new HashSet<>();

        getAllLibraries(libraries, memory);

        return libraries;
    }

    private void getAllLibraries(Set<Library> libraries, Set<SourceFile> memory){
        libraries.addAll(settings.getLibraries());

        for(Resources resources: settings.getResources()){
            if(resources.isValid()) {
                final Optional<SourceFile> target = resources.getTarget();

                if(target.isPresent() && memory.add(target.get())){
                    getAllLibraries(libraries, memory);
                }
            }
        }
    }

    private <T> Set<T> findNode(String suite, Token name, Set<SourceFile> memory, Class<T> type){
        HashSet<T> nodes = new HashSet<>();

        if(suite == null || suite.isEmpty() || matches(suite)){
            if(type == UserKeyword.class){
                nodes.addAll((Collection<? extends T>) userKeywordTable.findNode(name));
            }
            else if(type == TestCase.class){
                nodes.addAll((Collection<? extends T>) testCaseTable.findNode(name));
            }
            else if(type == VariableAssignment.class){
                nodes.addAll((Collection<? extends T>) variableTable.findNode(name));
            }
        }

        for(Resources resources: settings.getResources()){
            if(resources.isValid()) {
                final Optional<SourceFile> target = resources.getTarget();

                if(target.isPresent() && memory.add(target.get())){
                    nodes.addAll(target.get().findNode(suite, name, memory, type));
                }
            }
        }

        return nodes;
    }


    public void addLine(Line line) {
        lines.add(line);
    }

    public boolean isDirectDependency(SourceFile file) {
        if(file == null){
            return false;
        }

        if(this == file){
            return true;
        }

        for(Resources resources: settings.getResources()){
            if(resources.getSourceFile() == file){
                return true;
            }
        }

        return false;
    }

    public boolean isImportLibrary(String libraryName) {
        return this.settings.containsLibrary(libraryName);
    }

    public boolean matches(String path){
        if(path == null){
            return false;
        }

        if(path.equalsIgnoreCase(getPath())){
            return true;
        }

        return path.equalsIgnoreCase(FilenameUtils.getBaseName(getPath()));
    }

    @Override
    public List<Edit> differences(SourceNode other) {
        if(other == null){
            return Collections.singletonList(Edit.removeElement(this.getClass(), this));
        }

        if(other == this){
            return Collections.emptyList();
        }

        return Collections.emptyList();
    }
}
