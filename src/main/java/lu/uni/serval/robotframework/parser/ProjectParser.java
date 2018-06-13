package lu.uni.serval.robotframework.parser;

import lu.uni.serval.robotframework.model.Project;
import lu.uni.serval.robotframework.model.Settings;
import lu.uni.serval.robotframework.model.TestCaseFile;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;

public class ProjectParser {
    private ProjectParser(){}

    static public Project parse(String filePath){
        Project project = new Project();

        try {
            File file = new File(filePath);

            if(file.isFile()){
                readFile(file, project);
            }
            else if (file.isDirectory()){
                readFile(file, project);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return project;
    }

    static public void readFile(File file, Project project){
        try {
            FileReader input = new FileReader(file);
            BufferedReader bufferRead = new BufferedReader(input);

            TestCaseFile testCaseFile = new TestCaseFile();

            String line;
            while( (line = bufferRead.readLine()) != null){
                if(isSetting(line)){
                    Settings settings = SettingParser.parse(bufferRead);
                    testCaseFile.setSettings(settings);
                }
                else if(isTestCases(line)){

                }
                else if(isKeywords(line)){

                }
                else if(isVariable(line)){

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public void readFiles(File directory, Project project){
        throw new NotImplementedException();
    }

    static private boolean isSetting(String line){
        return isBlock(line, "setting");
    }

    static private boolean isTestCases(String line){
        return isBlock(line, "test cases");
    }

    static private boolean isKeywords(String line){
        return isBlock(line, "keywords");
    }

    static private boolean isVariable(String line){
        return isBlock(line, "variable");
    }

    static private boolean isBlock(String line, String block){
        String regex = String.format("^\\*\\*\\*(\\s*)%s(\\s*)\\*\\*\\*", block);
        return ParsingUtils.compareNoCase(line, regex);
    }
}
