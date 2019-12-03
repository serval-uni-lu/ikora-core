package org.ukwikora.builder;

import org.ukwikora.error.Error;
import org.ukwikora.exception.InvalidDependencyException;
import org.ukwikora.model.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

class SourceFileParser {
    public static void parse(File file, Project project, DynamicImports dynamicImports, List<Error> errors) {
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
                    NodeTable<TestCase> testCaseTable = TestCaseTableParser.parse(reader, dynamicImports);
                    sourceFile.setTestCaseTable(testCaseTable);
                }
                else if(isKeywords(text)){
                    NodeTable<UserKeyword> nodeTable = KeywordTableParser.parse(reader, dynamicImports);
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

            logger.trace("file parse: " + file.getAbsolutePath());
        } catch (IOException e) {
            sourceFile = new SourceFile(project, file);
            setName(project, sourceFile);

            project.addSourceFile(sourceFile);

            logger.error("failed to parse: " + file.getAbsolutePath());
        } catch (InvalidDependencyException e) {
            logger.error("Oops, something in the link between keywords call and definition went really bad: " + file.getAbsolutePath());
        } catch (Exception e) {
            logger.error(String.format("Oops, something went really wrong during parsing!: %s", e.getMessage()));
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
