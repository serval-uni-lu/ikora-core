package tech.ikora.model;

import org.apache.commons.io.FilenameUtils;
import tech.ikora.builder.Line;

import java.util.*;

public class SourceFile {
    final private Project project;
    final private Source source;
    final private List<Line> lines;

    private String name;
    private Settings settings;
    private SourceNodeTable<TestCase> testCaseTable;
    private SourceNodeTable<UserKeyword> userKeywordTable;
    private SourceNodeTable<VariableAssignment> variableTable;

    public SourceFile(Project project, Source source){
        this.project = project;
        this.source = source;
        this.lines = new ArrayList<>();

        setSettings(new Settings());
        setTestCaseTable(new SourceNodeTable<>());
        setKeywordTable(new SourceNodeTable<>());
        setVariableTable(new SourceNodeTable<>());

        String name = this.project.generateFileName(this.source);
        setName(name);
    }

    private void setName(String name) {
        this.name = name;

        this.settings.setFile(this);
        this.testCaseTable.setSourceFile(this);
        this.userKeywordTable.setSourceFile(this);
        this.variableTable.setSourceFile(this);
    }

    public List<Line> getLines(){
        return this.lines;
    }

    public Tokens getTokens() {
        Tokens fileTokens = new Tokens();

        fileTokens.addAll(testCaseTable.getTokens());
        fileTokens.addAll(userKeywordTable.getTokens());
        fileTokens.addAll(variableTable.getTokens());

        return fileTokens;
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

    public void setSettings(Settings settings) {
        this.settings = settings;
        this.settings.setFile(this);
    }

    public void setTestCaseTable(SourceNodeTable<TestCase> testCaseTable) {
        this.testCaseTable = testCaseTable;
        this.testCaseTable.setSourceFile(this);
    }

    public void setKeywordTable(SourceNodeTable<UserKeyword> nodeTable) {
        this.userKeywordTable = nodeTable;
        this.userKeywordTable.setSourceFile(this);
    }

    public void setVariableTable(SourceNodeTable<VariableAssignment> variableTable) {
        this.variableTable = variableTable;
        this.variableTable.setSourceFile(this);
    }

    public Project getProject() {
        return project;
    }

    public Source getSource() {
        return source;
    }

    public String getDirectory(){
        return this.source.getDirectory();
    }

    public String getPath() {
        return this.source.getPath();
    }

    public String getName() {
        return this.name;
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

    public Set<Variable> findVariable(Token name) {
        return findNode(null, name, new HashSet<>(), Variable.class);
    }

    public Set<Variable> findVariable(String library, Token name) {
        return findNode(library, name, new HashSet<>(), Variable.class);
    }

    private <T> Set<T> findNode(String library, Token name, Set<SourceFile> memory, Class<T> type){
        HashSet<T> nodes = new HashSet<>();

        if(library == null || library.isEmpty() || matches(library)){
            if(type == UserKeyword.class){
                nodes.addAll((Collection<? extends T>) userKeywordTable.findNode(name));
            }

            if(type == TestCase.class){
                nodes.addAll((Collection<? extends T>) testCaseTable.findNode(name));
            }

            if(type == Variable.class){
                nodes.addAll((Collection<? extends T>) variableTable.findNode(name));
            }
        }

        for(Resources resources: settings.getResources()){
            if(resources.isValid() && memory.add(resources.getSourceFile())) {
                SourceFile file = resources.getSourceFile();
                nodes.addAll(file.findNode(library, name, memory, type));
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
}
