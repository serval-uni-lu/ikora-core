package lu.uni.serval.robotframework.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Project {
    private Set<TestCaseFile> testCaseFiles;
    private Map<String, TestCaseFile> files;

    public Project(){
        testCaseFiles = new HashSet<>();
        files = new HashMap<>();
    }

    public boolean hasFile(String filePath){
        return files.containsKey(filePath);
    }

    public Set<TestCaseFile> getTestCaseFiles(){
        return testCaseFiles;
    }

    public TestCaseFile getFile(String path){
        return files.get(path);
    }

    public Map<String, TestCaseFile> getFiles(){
        return files;
    }

    public void addFile(String path){
        if(hasFile(path)){
            return;
        }

        files.put(path, null);
    }

    public void addTestCaseFile(TestCaseFile file){
        testCaseFiles.add(file);

        files.put(file.getPath(), file);

        for(Resources resource: file.getSettings().getResources()){
            files.put(resource.getFile().getPath(), resource.getFile());
        }

        updateFiles(file.getSettings());
    }

    private void updateFiles(Settings settings){
        for(Resources resources: settings.getResources()){
            addFile(resources.getName());
        }
    }
}
