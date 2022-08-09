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

import lu.uni.serval.ikora.core.model.Literal;
import lu.uni.serval.ikora.core.model.NodeList;
import lu.uni.serval.ikora.core.model.Scope;
import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.utils.StringUtils;

import java.util.Iterator;

public class TagsParser {
    public enum Type {
        NORMAL,
        DEFAULT,
        FORCE
    }

    private TagsParser() {}

    public static boolean is(Token label, Scope scope, Type type){
        String typeString;

        switch (type){
            case NORMAL: typeString = ""; break;
            case DEFAULT: typeString = "default "; break;
            case FORCE: typeString = "force "; break;
            default: return false;
        }

        String expression = typeString + "tags";
        if(scope == Scope.KEYWORD) expression = "\\[" + expression + "\\]";

        return StringUtils.matchesIgnoreCase(label, expression);
    }

    public static NodeList<Literal> parse(Token label, Iterator<Token> tokenIterator) {
        NodeList<Literal> tags = new NodeList<>(label.setType(Token.Type.LABEL));

        while(tokenIterator.hasNext()){
            tags.add(new Literal(tokenIterator.next()));
        }

        return tags;
    }
}
