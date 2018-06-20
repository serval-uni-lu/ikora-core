package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class TestCaseFileParser {
    static public void parse(File file, Project project) {
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
    }

    static private boolean isSettings(String line){
        return Utils.isBlock(line, "settings");
    }

    static private boolean isTestCases(String line){
        return Utils.isBlock(line, "test cases");
    }

    static private boolean isKeywords(String line){
        return Utils.isBlock(line, "keywords");
    }

    static private boolean isVariable(String line){
        return Utils.isBlock(line, "variables");
    }
}
