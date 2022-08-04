package lu.uni.serval.ikora.core.parser;

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

import lu.uni.serval.ikora.core.builder.DynamicImports;
import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.model.SourceNodeTable;
import lu.uni.serval.ikora.core.model.TestCase;
import lu.uni.serval.ikora.core.model.Tokens;

import java.io.IOException;

class TestCaseTableParser {
    private TestCaseTableParser() {}

    public static SourceNodeTable<TestCase> parse(LineReader reader, Tokens blockTokens, DynamicImports dynamicImports, ErrorManager errors) throws IOException {
        SourceNodeTable<TestCase> testCaseTable = new SourceNodeTable<>();
        testCaseTable.setHeader(ParserUtils.parseHeaderName(reader, blockTokens, errors));

        while(reader.getCurrent().isValid() && !LexerUtils.isBlock(reader.getCurrent().getText())){
            if(reader.getCurrent().ignore()){
                reader.readLine();
                continue;
            }

            final Tokens nameTokens = LexerUtils.tokenize(reader);
            final TestCase testCase = TestCaseParser.parse(reader, nameTokens, dynamicImports, errors);
            testCaseTable.add(testCase);
        }

        return testCaseTable;
    }
}
