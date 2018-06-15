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

        try {
            File file = new File(filePath);

            if(file.isFile()){
                readFile(file, project);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return project;
    }

    static public void readFile(File file, Project project){
        if(file == null){
            return;
        }

        try {
            FileReader input = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(input);

            TestCaseFile testCaseFile = new TestCaseFile();
            testCaseFile.setFile(file);

            String line = bufferedReader.readLine();
            while(line != null){
                if(isSettings(line)){
                    Settings settings = new Settings();
                    settings.setFile(testCaseFile.getFile());
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
                    line = bufferedReader.readLine();
                }
            }

            project.addTestCaseFile(testCaseFile);

        } catch (IOException e) {
            e.printStackTrace();
        }

        readFile(getUnparsedFiles(project), project);
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

    static private File getUnparsedFiles(Project project){
        for (Map.Entry<File, TestCaseFile> file: project.getFiles().entrySet()){
            if(file.getValue() == null){
                return file.getKey();
            }
        }

        return null;
    }
}
