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
            logger.trace("file parse: " + file.getAbsolutePath());

        } catch (IOException e) {
            TestCaseFile testCaseFile = new TestCaseFile();
            testCaseFile.setFile(file);
            project.addTestCaseFile(testCaseFile);

            logger.error("failed to parse: " + file.getAbsolutePath());
        }
        finally {
            if(reader != null){
                reader.close();
            }
        }
    }

    static private boolean isSettings(String line){
        return Utils.isBlock(line, "setting(s?)");
    }

    static private boolean isTestCases(String line){
        return Utils.isBlock(line, "test case(s?)");
    }

    static private boolean isKeywords(String line){
        return Utils.isBlock(line, "keyword(s?)");
    }

    static private boolean isVariable(String line){
        return Utils.isBlock(line, "variable(s?)");
    }
}
