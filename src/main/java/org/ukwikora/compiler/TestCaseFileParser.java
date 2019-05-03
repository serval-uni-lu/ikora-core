package org.ukwikora.compiler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ukwikora.exception.InvalidDependencyException;
import org.ukwikora.model.*;

import java.io.File;
import java.io.IOException;

class TestCaseFileParser {
    private static final Logger logger = LogManager.getLogger(TestCaseFileParser.class);

    public static void parse(File file, Project project, DynamicImports dynamicImports) {
        LineReader reader = null;
        TestCaseFile testCaseFile = null;

        try {
            testCaseFile = new TestCaseFile(project, file);
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
                    Settings settings = SettingsTableParser.parse(reader, testCaseFile);
                    testCaseFile.setSettings(settings);
                }
                else if(isTestCases(text)){
                    StatementTable<TestCase> testCaseTable = TestCaseTableParser.parse(reader, dynamicImports);
                    testCaseFile.setTestCaseTable(testCaseTable);
                }
                else if(isKeywords(text)){
                    StatementTable<UserKeyword> statementTable = KeywordTableParser.parse(reader, dynamicImports);
                    testCaseFile.setKeywordTable(statementTable);
                }
                else if(isVariable(text)){
                    StatementTable<Variable> variableTable = VariableTableParser.parse(reader);
                    testCaseFile.setVariableTable(variableTable);
                }
                else {
                    reader.readLine();
                }
            }

            logger.trace("file parse: " + file.getAbsolutePath());
        } catch (IOException e) {
            testCaseFile = new TestCaseFile(project, file);
            setName(project, testCaseFile);

            project.addTestCaseFile(testCaseFile);

            logger.error("failed to parse: " + file.getAbsolutePath());
        } catch (InvalidDependencyException e) {
            logger.error("Oops, something in the link between keywords call and definition went really bad: " + file.getAbsolutePath());
        } catch (Exception e) {
            logger.error(String.format("Oops, something went really wrong during parsing!: %s", e.getMessage()));
        } finally {
            project.addTestCaseFile(testCaseFile);

            if(reader != null){
                reader.close();
            }
        }
    }

    private static void setName(Project project, TestCaseFile testCaseFile) {
        String name = project.generateFileName(testCaseFile.getFile());
        testCaseFile.setName(name);
    }

    static boolean isSettings(String line){
        return LexerUtils.isBlock(line, "setting(s?)");
    }

    static boolean isTestCases(String line){
        return LexerUtils.isBlock(line, "test case(s?)");
    }

    static boolean isKeywords(String line){
        return LexerUtils.isBlock(line, "keyword(s?)");
    }

    static boolean isVariable(String line){
        return LexerUtils.isBlock(line, "variable(s?)");
    }
}
