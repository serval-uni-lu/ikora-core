package org.ukwikora.builder;

import org.ukwikora.error.ErrorManager;
import org.ukwikora.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

class SourceFileParser {
    public static void parse(File file, Project project, DynamicImports dynamicImports, ErrorManager errors) {
        LineReader reader = null;
        SourceFile sourceFile = null;

        try {
            sourceFile = new SourceFile(project, file);
            reader = new LineReader(sourceFile);

            setName(project, sourceFile);

            reader.readLine();

            while(reader.getCurrent().isValid()){
                if(reader.getCurrent().ignore()){
                    reader.readLine();
                    continue;
                }

                String text = reader.getCurrent().getText();

                if(isSettings(text)){
                    Settings settings = SettingsTableParser.parse(reader, sourceFile);
                    sourceFile.setSettings(settings);
                }
                else if(isTestCases(text)){
                    NodeTable<TestCase> testCaseTable = TestCaseTableParser.parse(reader, dynamicImports, errors);
                    sourceFile.setTestCaseTable(testCaseTable);
                }
                else if(isKeywords(text)){
                    NodeTable<UserKeyword> nodeTable = KeywordTableParser.parse(reader, dynamicImports, errors);
                    sourceFile.setKeywordTable(nodeTable);
                }
                else if(isVariable(text)){
                    NodeTable<Variable> variableTable = VariableTableParser.parse(reader);
                    sourceFile.setVariableTable(variableTable);
                }
                else {
                    reader.readLine();
                }
            }
        } catch (FileNotFoundException e) {
            errors.registerIOError("File not found", file);
        } catch (IOException e) {
            errors.registerIOError("Failed to read line", file);
        } finally {
            project.addSourceFile(sourceFile);

            if(reader != null){
                reader.close();
            }
        }
    }

    private static void setName(Project project, SourceFile sourceFile) {
        String name = project.generateFileName(sourceFile.getFile());
        sourceFile.setName(name);
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
