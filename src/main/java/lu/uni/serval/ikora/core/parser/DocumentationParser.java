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

import lu.uni.serval.ikora.core.model.Documentation;
import lu.uni.serval.ikora.core.model.Scope;
import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.utils.StringUtils;
import lu.uni.serval.ikora.core.utils.TokenUtils;

import java.util.Iterator;

public class DocumentationParser {
    private DocumentationParser() {}

    public static boolean is(Token label, Scope scope){
        final String expression = scope == Scope.KEYWORD ? "\\[documentation\\]" : "documentation";
        return StringUtils.matchesIgnoreCase(label, expression);
    }

    public static Documentation parse(Token label, Iterator<Token> iterator){
        return new Documentation(label, TokenUtils.accumulate(iterator));
    }
}
