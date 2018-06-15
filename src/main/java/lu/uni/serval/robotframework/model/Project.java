package lu.uni.serval.robotframework.model;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Project {
    private Set<TestCaseFile> testCaseFiles;
    private Map<File, TestCaseFile> files;

    public Project(){
        testCaseFiles = new HashSet<>();
        files = new HashMap<>();
    }

    public boolean hasFile(File file){
        return files.containsKey(file);
    }

    public Set<TestCaseFile> getTestCaseFiles(){
        return testCaseFiles;
    }

    public TestCaseFile getFile(String path){
        return files.get(path);
    }

    public Map<File, TestCaseFile> getFiles(){
        return files;
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
