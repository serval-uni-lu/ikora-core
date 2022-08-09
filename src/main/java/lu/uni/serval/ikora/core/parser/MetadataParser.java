/*
 *
 *     Copyright © 2019 - 2022 University of Luxembourg
 *
 *     Licensed under the Apache License, Version 2.0 (the "License")
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package lu.uni.serval.ikora.core.parser;

import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.error.ErrorMessages;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.utils.StringUtils;
import lu.uni.serval.ikora.core.utils.TokenUtils;

import java.util.Iterator;

public class MetadataParser {
    private MetadataParser() {}

    public static boolean is(Token label){
        return StringUtils.matchesIgnoreCase(label, "metadata");
    }

    public static Metadata parse(LineReader reader, Token label, Iterator<Token> tokenIterator, ErrorManager errors) {
        final Token key = tokenIterator.hasNext() ? tokenIterator.next() : Token.empty();

        if(!tokenIterator.hasNext()){
            errors.registerSyntaxError(
                    reader.getSource(),
                    ErrorMessages.MISSING_METADATA_VALUE,
                    Range.fromToken(label)
            );
        }

        final Value value = tokenIterator.hasNext() ? ValueParser.parseValue(tokenIterator.next()) : new Literal(Token.empty());

        if(tokenIterator.hasNext()){
            errors.registerSyntaxError(
                    reader.getSource(),
                    ErrorMessages.TOO_MANY_METADATA_ARGUMENTS,
                    Range.fromTokens(TokenUtils.accumulate(tokenIterator))
            );
        }

        return new Metadata(label, key, value);
    }
}
