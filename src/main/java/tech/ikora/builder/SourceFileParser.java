package tech.ikora.builder;

import tech.ikora.error.ErrorManager;
import tech.ikora.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

class SourceFileParser {
    private SourceFileParser() {}

    public static void parse(File file, Project project, DynamicImports dynamicImports, ErrorManager errors) {
        LineReader reader = null;
        SourceFile sourceFile = null;

        try {
            sourceFile = new SourceFile(project, file);
            reader = new LineReader(sourceFile);
            reader.readLine();

            while(reader.getCurrent().isValid()){
                if(reader.getCurrent().ignore()){
                    reader.readLine();
                    continue;
                }

                Tokens tokens = LexerUtils.tokenize(reader);

                if(isSettings(tokens.toString())){
                    Settings settings = SettingsTableParser.parse(reader, tokens, sourceFile, errors);
                    sourceFile.setSettings(settings);
                }
                else if(isTestCases(tokens.toString())){
                    NodeTable<TestCase> testCaseTable = TestCaseTableParser.parse(reader, tokens, dynamicImports, errors);
                    sourceFile.setTestCaseTable(testCaseTable);
                }
                else if(isKeywords(tokens.toString())){
                    NodeTable<UserKeyword> nodeTable = KeywordTableParser.parse(reader, tokens, dynamicImports, errors);
                    sourceFile.setKeywordTable(nodeTable);
                }
                else if(isVariable(tokens.toString())){
                    NodeTable<Variable> variableTable = VariableTableParser.parse(reader, tokens, errors);
                    sourceFile.setVariableTable(variableTable);
                }
            }
        } catch (FileNotFoundException e) {
            errors.registerIOError(file, "File not found");
        } catch (IOException e) {
            errors.registerIOError(file, "Failed to read line");
        } finally {
            project.addSourceFile(sourceFile);

            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    errors.registerIOError(file,"Failed to properly close reader for file %s");
                }
            }
        }
    }

    static boolean isSettings(String block){
        return LexerUtils.isBlock(block, "setting(s?)");
    }

    static boolean isTestCases(String block){
        return LexerUtils.isBlock(block, "test case(s?)");
    }

    static boolean isKeywords(String block){
        return LexerUtils.isBlock(block, "keyword(s?)");
    }

    static boolean isVariable(String block){
        return LexerUtils.isBlock(block, "variable(s?)");
    }
}
