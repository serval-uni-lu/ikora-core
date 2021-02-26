package lu.uni.serval.ikora.core.builder;

import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.model.*;

import java.io.FileNotFoundException;
import java.io.IOException;

class SourceFileParser {
    private SourceFileParser() {}

    public static void parse(Source source, Project project, DynamicImports dynamicImports, ErrorManager errors) {
        LineReader reader = null;
        SourceFile sourceFile = null;

        try {
            sourceFile = new SourceFile(project, source);
            reader = new LineReader(sourceFile);
            reader.readLine();

            while(reader.getCurrent().isValid()){
                if(reader.getCurrent().ignore()){
                    reader.readLine();
                    continue;
                }

                Tokens tokens = LexerUtils.tokenize(reader);

                if(isSettings(tokens.toString())){
                    Settings settings = SettingsTableParser.parse(reader, tokens, errors);
                    sourceFile.setSettings(settings);
                }
                else if(isTestCases(tokens.toString())){
                    SourceNodeTable<TestCase> testCaseTable = TestCaseTableParser.parse(reader, tokens, dynamicImports, errors);
                    sourceFile.setTestCaseTable(testCaseTable);
                }
                else if(isKeywords(tokens.toString())){
                    SourceNodeTable<UserKeyword> nodeTable = KeywordTableParser.parse(reader, tokens, dynamicImports, errors);
                    sourceFile.setKeywordTable(nodeTable);
                }
                else if(isVariable(tokens.toString())){
                    SourceNodeTable<VariableAssignment> variableTable = VariableTableParser.parse(reader, tokens, errors);
                    sourceFile.setVariableTable(variableTable);
                }
            }
        } catch (FileNotFoundException e) {
            errors.registerIOError(source, "File not found");
        } catch (IOException e) {
            errors.registerIOError(source, "Failed to read line");
        } finally {
            project.addSourceFile(sourceFile);

            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    errors.registerIOError(source,"Failed to properly close reader for file %s");
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
