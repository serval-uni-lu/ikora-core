package org.ukwikora.compiler;

import org.apache.log4j.Logger;
import org.ukwikora.model.*;

import java.io.File;
import java.io.IOException;

public class TestCaseFileParser {
    final static Logger logger = Logger.getLogger(TestCaseFileParser.class);

    static public void parse(File file, Project project) {
        LineReader reader = null;

        try {
            TestCaseFile testCaseFile = new TestCaseFile(project, file);
            reader = new LineReader(testCaseFile);

            setName(project, testCaseFile);

            reader.readLine();

            while(reader.getCurrent().isValid()){
                if(reader.getCurrent().ignore()){
                    reader.readLine();
                    continue;
                }

                String text = reader.getCurrent().getText();

                if(isSettings(text)){
                    Settings settings = SettingsTableParser.parse(reader);
                    testCaseFile.setSettings(settings);
                }
                else if(isTestCases(text)){
                    ElementTable<TestCase> testCaseTable = TestCaseTableParser.parse(reader);
                    testCaseFile.setTestCaseTable(testCaseTable);
                }
                else if(isKeywords(text)){
                    ElementTable<UserKeyword> elementTable = KeywordTableParser.parse(reader);
                    testCaseFile.setKeywordTable(elementTable);
                }
                else if(isVariable(text)){
                    ElementTable<Variable> variableTable = VariableTableParser.parse(reader);
                    testCaseFile.setVariableTable(variableTable);
                }
                else {
                    reader.readLine();
                }
            }

            project.addTestCaseFile(testCaseFile);
            logger.trace("file parse: " + file.getAbsolutePath());

        } catch (IOException e) {
            TestCaseFile testCaseFile = new TestCaseFile(project, file);
            setName(project, testCaseFile);

            project.addTestCaseFile(testCaseFile);

            logger.error("failed to parse: " + file.getAbsolutePath());
        }
        finally {
            if(reader != null){
                reader.close();
            }
        }
    }

    private static void setName(Project project, TestCaseFile testCaseFile) {
        String name = project.generateFileName(testCaseFile.getFile());
        testCaseFile.setName(name);
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
