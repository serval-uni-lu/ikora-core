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
package lu.uni.serval.ikora.core.utils;

import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.model.Tokens;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Iterator;
import java.util.Optional;

public class TokenUtils {
    private TokenUtils() {}

    public static Tokens accumulate(Iterator<Token> tokenIterator){
        final Tokens tokens = new Tokens();
        while (tokenIterator != null && tokenIterator.hasNext()) tokens.add(tokenIterator.next());

        return tokens;
    }

    public static Token trimRightEquals(Token token) {
        final Optional<Pair<Integer, Integer>> equal = StringUtils.findEqual(token.getText());

        if(equal.isPresent() && equal.get().getRight() == token.getText().length()){
            return token.extract(0, equal.get().getLeft());
        }

        return token.copy();
    }

    public static Token extractEqualSign(Token token) {
        final Optional<Pair<Integer, Integer>> equal = StringUtils.findEqual(token.getText());

        if(equal.isPresent()){
            return token.extract(equal.get().getLeft(), equal.get().getRight());
        }

        return Token.empty();
    }
}
