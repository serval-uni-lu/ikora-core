package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class TestCaseFileParser {
    final static Logger logger = Logger.getLogger(TestCaseFileParser.class);

    static public void parse(File file, Project project) {
        LineReader reader = null;

        try {
            reader = new LineReader(file);

            TestCaseFile testCaseFile = new TestCaseFile();
            testCaseFile.setFile(file);

            reader.readLine();

            while(reader.getCurrent().isValid()){
                String text = reader.getCurrent().getText();

                if(isSettings(text)){
                    Settings settings = SettingsTableParser.parse(reader);
                    testCaseFile.setSettings(settings);
                }
                else if(isTestCases(text)){
                    TestCaseTable testCaseTable = TestCaseTableParser.parse(reader);
                    testCaseFile.setTestCaseTable(testCaseTable);
                }
                else if(isKeywords(text)){
                    KeywordTable keywordTable = KeywordTableParser.parse(reader);
                    testCaseFile.setKeywordTable(keywordTable);
                }
                else if(isVariable(text)){
                    VariableTable variableTable = VariableTableParser.parse(reader);
                    testCaseFile.setVariableTable(variableTable);
                }
                else {
                    reader.readLine();
                }
            }

            project.addTestCaseFile(testCaseFile);
            logger.info("file parse: " + file.getAbsolutePath());

        } catch (IOException e) {
            logger.error("failed to parse: " + file.getAbsolutePath());
        }
        finally {
            reader.close();
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
