package lu.uni.serval.robotframework.model;

import lu.uni.serval.utils.tree.LabelTreeNode;

import java.io.File;
import java.util.*;

public class Project {
    private List<TestCaseFile> testCaseFiles;
    private Map<File, TestCaseFile> files;
    private LibraryResources libraries;

    public Project(){
        testCaseFiles = new ArrayList<>();
        files = new HashMap<>();
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

    public LibraryResources getLibraries() {
        return libraries;
    }

    public List<UserKeyword> getKeywords() {
        if(testCaseFiles.isEmpty()) {
            return new ArrayList<>();
        }

        return testCaseFiles.get(0).getUserKeywords();
    }

    public Set<LabelTreeNode> getKeywordNodes() {
        Set<LabelTreeNode> nodes = new HashSet<>();

        for(UserKeyword keyword: getKeywords()) {
            nodes.add(keyword.getNode());
        }

        return nodes;
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
}
