package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.*;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.IOException;

import java.util.Map;

public class ProjectParser {
    private ProjectParser(){}

    static public Project parse(String filePath){
        Project project = new Project();

        try {
            File file = new File(filePath);

            if(file.isFile()){
                parseFiles(file, project);
                resolveResources(project);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return project;
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
