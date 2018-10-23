package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.*;
import lu.uni.serval.robotframework.utils.Configuration;
import lu.uni.serval.robotframework.utils.Plugin;
import org.apache.commons.io.FileUtils;

import java.io.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProjectParser {
    private ProjectParser(){}

    static public Project parse(String filePath){
        Project project = new Project(filePath);

        try {
            File file = new File(filePath);

            if(file.isDirectory()){
                file = addRobotFiles(file, project);
            }

            parseFiles(file, project);
            resolveResources(project);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return project;
    }

    private static File addRobotFiles(File directory, Project project) {
        String[] extensions = new String[] { "robot" };
        List<File> robots = (List<File>) FileUtils.listFiles(directory, extensions, true);

        List<File> ignoreList = getIgnoreList(project);

        for(File robot: robots){
            if(!isIgnored(robot, ignoreList)){
                project.addFile(robot);
            }
        }

        return getNextNotParsedFile(project);
    }

    static private void parseFiles(File file, Project project){
        if(file == null){
            return;
        }

        TestCaseFileParser.parse(file, project);

        parseFiles(getNextNotParsedFile(project), project);
    }

    static private void resolveResources(Project project) throws Exception {
        for(TestCaseFile testCaseFile: project.getTestCaseFiles()) {
            for (Resources resources: testCaseFile.getSettings().getResources()) {
                String name = project.generateFileName(resources.getFile());
                TestCaseFile resourceFile = project.getTestCaseFile(name);

                if(resourceFile == null) {
                    throw new Exception();
                }

                resources.setTestCasefile(resourceFile);
            }
        }
    }

    static private File getNextNotParsedFile(Project project){
        for (Map.Entry<String, TestCaseFile> file: project.getFiles().entrySet()){
            if(file.getValue() == null){
                return new File(project.getRootFolder(), file.getKey());
            }
        }

        return null;
    }

    static private List<File> getIgnoreList(Project project){
        Configuration configuration = Configuration.getInstance();
        Plugin plugin = configuration.getPlugin("project analytics");

        List<String> paths = (List<String>) plugin.getAdditionalProperty("exclude folders", new ArrayList<String>());
        List<File> ignoreList = new ArrayList<>();

        for(String path: paths){
            ignoreList.add(new File(project.getRootFolder(), path));
        }

        return ignoreList;
    }

    static private boolean isIgnored(File file, List<File> ignoreList){
        for(File ignoreFolder: ignoreList){
            if(isInSubDirectory(ignoreFolder, file)){
                return true;
            }
        }

        return false;
    }

    public static boolean isInSubDirectory(File dir, File file) {
        if (file == null || dir == null){
            return false;
        }

        if (file.equals(dir)){
            return true;
        }

        return isInSubDirectory(dir, file.getParentFile());
    }
}
