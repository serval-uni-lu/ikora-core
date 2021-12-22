package lu.uni.serval.ikora.core.utils;

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

import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.model.Tokens;

import java.util.Iterator;
import java.util.regex.Pattern;

public class TokenUtils {
    public static final Pattern equalsFinder = Pattern.compile("(\\s*=\\s*)$");

    private TokenUtils() {}

    public static Tokens accumulate(Iterator<Token> tokenIterator){
        final Tokens tokens = new Tokens();
        while (tokenIterator != null && tokenIterator.hasNext()) tokens.add(tokenIterator.next());

        return tokens;
    }

    public static Token trimRightEquals(Token token) {
        return token.trim(equalsFinder);
    }

    public static Token extractEqualSign(Token token) {
        return token.extract(equalsFinder);
    }
}
