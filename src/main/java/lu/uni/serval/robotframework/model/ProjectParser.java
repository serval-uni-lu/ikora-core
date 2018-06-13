package lu.uni.serval.robotframework.model;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProjectParser {
    private ProjectParser(){}

    public Project parse(String filePath){
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

    public void readFile(File file, Project project){
        try {
            FileReader input = new FileReader(file);
            BufferedReader bufRead = new BufferedReader(input);

            String line;
            while( (line = bufRead.readLine()) != null){
                if(isSetting(line)){

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

    public void readFiles(File directory, Project project){
        throw new NotImplementedException();
    }

    private boolean isSetting(String line){
        return isBlock(line, "setting");
    }

    private boolean isTestCases(String line){
        return isBlock(line, "test cases");
    }

    private boolean isKeywords(String line){
        return isBlock(line, "keywords");
    }

    private boolean isVariable(String line){
        return isBlock(line, "variable");
    }



    private boolean isBlock(String line, String block){
        String regex = String.format("^\\*\\*\\*(\\s*)%s(\\s*)\\*\\*\\*", block);
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);

        return matcher.matches();
    }
}
