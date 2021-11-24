package lu.uni.serval.ikora.core.builder;

/*-
 * #%L
 * Ikora Core
 * %%
 * Copyright (C) 2019 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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

                final Tokens tokens = LexerUtils.tokenize(reader);

                if(isSettings(tokens.toString())){
                    final Settings settings = SettingsTableParser.parse(reader, tokens, errors);
                    sourceFile.setSettings(settings);
                }
                else if(isTestCases(tokens.toString())){
                    final SourceNodeTable<TestCase> testCaseTable = TestCaseTableParser.parse(reader, tokens, dynamicImports, errors);
                    sourceFile.setTestCaseTable(testCaseTable);
                }
                else if(isKeywords(tokens.toString())){
                    final SourceNodeTable<UserKeyword> nodeTable = KeywordTableParser.parse(reader, tokens, dynamicImports, errors);
                    sourceFile.setKeywordTable(nodeTable);
                }
                else if(isVariable(tokens.toString())){
                    final SourceNodeTable<VariableAssignment> variableTable = VariableTableParser.parse(reader, tokens, errors);
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
