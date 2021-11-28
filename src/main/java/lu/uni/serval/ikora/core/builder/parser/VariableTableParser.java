package lu.uni.serval.ikora.core.builder.parser;

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
import lu.uni.serval.ikora.core.model.SourceNodeTable;
import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.model.Tokens;
import lu.uni.serval.ikora.core.model.VariableAssignment;

import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;

class VariableTableParser {
    private VariableTableParser() {}

    public static SourceNodeTable<VariableAssignment> parse(LineReader reader, Tokens blockTokens, ErrorManager errors) throws IOException {
        final SourceNodeTable<VariableAssignment> variableTable = new SourceNodeTable<>();
        variableTable.setHeader(ParserUtils.parseHeaderName(reader, blockTokens, errors));

        while(reader.getCurrent().isValid() && !LexerUtils.isBlock(reader.getCurrent().getText())){
            if(reader.getCurrent().ignore()){
                reader.readLine();
                continue;
            }

            final Tokens lineTokens = LexerUtils.tokenize(reader);
            final Iterator<Token> tokenIterator = TokenScanner.from(lineTokens)
                    .skipIndent(true)
                    .iterator();

            final Optional<VariableAssignment> variable = VariableAssignmentParser.parse(reader, tokenIterator, errors);
            variable.ifPresent(variableTable::add);
        }

        return variableTable;
    }
}
