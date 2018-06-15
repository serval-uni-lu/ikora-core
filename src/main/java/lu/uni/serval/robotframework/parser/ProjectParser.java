package lu.uni.serval.robotframework.parser;

import lu.uni.serval.robotframework.model.Project;
import lu.uni.serval.robotframework.model.Settings;
import lu.uni.serval.robotframework.model.TestCaseFile;
import lu.uni.serval.robotframework.model.TestCaseTable;

import java.io.*;
import java.util.Map;

public class ProjectParser {
    private ProjectParser(){}

    static public Project parse(String filePath){
        Project project = new Project();

        return parse(filePath, project);
    }

    static private Project parse(String filePath, Project project){
        if(filePath == null){
            return project;
        }

        try {
            File file = new File(filePath);

            if(file.isFile()){
                readFile(file, project);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return parse(getUnparsedFiles(project), project);
    }

    static public void readFile(File file, Project project){
        try {
            FileReader input = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(input);

            TestCaseFile testCaseFile = new TestCaseFile();

            String line = bufferedReader.readLine();
            while(line != null){
                if(isSettings(line)){
                    Settings settings = new Settings();
                    line = SettingsTableParser.parse(bufferedReader, settings);
                    testCaseFile.setSettings(settings);
                }
                else if(isTestCases(line)){
                    TestCaseTable testCaseTable = new TestCaseTable();
                    line = TestCaseTableParser.parse(bufferedReader, testCaseTable);
                    testCaseFile.setTestCaseTable(testCaseTable);
                }
                else if(isKeywords(line)){
                    line = bufferedReader.readLine();
                }
                else if(isVariable(line)){
                    line = bufferedReader.readLine();
                }
                else {
                    bufferedReader.readLine();
                }
            }

            project.addTestCaseFile(testCaseFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static private boolean isSettings(String line){
        return ParsingUtils.isBlock(line, "settings");
    }

    static private boolean isTestCases(String line){
        return ParsingUtils.isBlock(line, "test cases");
    }

    static private boolean isKeywords(String line){
        return ParsingUtils.isBlock(line, "keywords");
    }

    static private boolean isVariable(String line){
        return ParsingUtils.isBlock(line, "variable");
    }

    static private String getUnparsedFiles(Project project){
        for (Map.Entry<String, TestCaseFile> file: project.getFiles().entrySet()){
            if(file.getValue() == null){
                return file.getKey();
            }
        }

        return null;
    }
}
