package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.*;
import org.apache.commons.io.FileUtils;

import java.io.*;

import java.util.List;
import java.util.Map;

public class ProjectParser {
    private ProjectParser(){}

    static public Project parse(String filePath){
        Project project = new Project();

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

        for(File robot: robots){
            project.addFile(robot);
        }

        return robots.size() > 0 ? robots.get(0) : null;
    }

    static private void parseFiles(File file, Project project){
        if(file == null){
            return;
        }

        TestCaseFileParser.parse(file, project);

        parseFiles(getUnparsedFiles(project), project);
    }

    static private void resolveResources(Project project) throws Exception {
        for(TestCaseFile testCaseFile: project.getTestCaseFiles()) {
            for (Resources resources: testCaseFile.getSettings().getResources()) {
                TestCaseFile resourceFile = project.getTestCaseFile(resources.getFile());

                if(resourceFile == null) {
                    throw new Exception();
                }

                resources.setTestCasefile(resourceFile);
            }
        }
    }

    static private File getUnparsedFiles(Project project){
        for (Map.Entry<File, TestCaseFile> file: project.getFiles().entrySet()){
            if(file.getValue() == null){
                return file.getKey();
            }
        }

        return null;
    }
}
