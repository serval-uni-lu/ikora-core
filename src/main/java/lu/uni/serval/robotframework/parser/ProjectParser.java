package lu.uni.serval.robotframework.parser;

import lu.uni.serval.robotframework.model.*;
import org.reflections.Reflections;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.IOException;

import java.util.Map;
import java.util.Set;

public class ProjectParser {
    private ProjectParser(){}

    static public Project parse(String filePath){
        Project project = new Project();

        try {
            File file = new File(filePath);

            if(file.isFile()){
                process(file, project);
                postProcess(project);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return project;
    }

    static private void process(File file, Project project){
        if(file == null){
            return;
        }

        Reflections reflections = new Reflections("lu.uni.serval.robotframework.libraries");
        Set<Class<? extends LibraryKeyword>> classes = reflections.getSubTypesOf(LibraryKeyword.class);

        try {
            FileReader input = new FileReader(file);
            LineNumberReader reader = new LineNumberReader(input);

            TestCaseFile testCaseFile = new TestCaseFile();
            testCaseFile.setFile(file);

            Line line = Line.getNextLine(reader);
            while(line.isValid()){
                if(isSettings(line.getText())){
                    Settings settings = new Settings();
                    settings.setFile(testCaseFile.getFile());
                    line = SettingsTableParser.parse(reader, settings);
                    testCaseFile.setSettings(settings);
                }
                else if(isTestCases(line.getText())){
                    TestCaseTable testCaseTable = new TestCaseTable();
                    line = TestCaseTableParser.parse(reader, testCaseTable);
                    testCaseFile.setTestCaseTable(testCaseTable);
                }
                else if(isKeywords(line.getText())){
                    KeywordTable keywordTable = new KeywordTable();
                    line = KeywordTableParser.parse(reader, keywordTable);
                    testCaseFile.setKeywordTable(keywordTable);
                }
                else if(isVariable(line.getText())){
                    VariableTable variableTable = new VariableTable();
                    line = VariableTableParser.parse(reader, variableTable);
                    testCaseFile.setVariableTable(variableTable);
                }
                else {
                    line = Line.getNextLine(reader);
                }
            }

            project.addTestCaseFile(testCaseFile);

        } catch (IOException e) {
            e.printStackTrace();
        }

        process(getUnparsedFiles(project), project);
    }

    static private void postProcess(Project project) throws Exception {
        resolveResources(project);
        KeywordLinker.link(project);
        VariableLinker.link(project);
    }

    private static void resolveResources(Project project) throws Exception {
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
        return ParsingUtils.isBlock(line, "variables");
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
