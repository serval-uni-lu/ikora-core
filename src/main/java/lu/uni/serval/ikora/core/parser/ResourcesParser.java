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

import lu.uni.serval.ikora.core.model.NodeList;
import lu.uni.serval.ikora.core.model.Resources;
import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.model.Value;
import lu.uni.serval.ikora.core.utils.StringUtils;

import java.util.Iterator;

public class ResourcesParser {
    private ResourcesParser() {}

    public static boolean is(Token label){
        return StringUtils.matchesIgnoreCase(label, "resource");
    }

    public static Resources parse(Token label, Iterator<Token> tokenIterator) {
        final Token filePath = tokenIterator.hasNext() ? tokenIterator.next() : Token.empty();
        final NodeList<Value> arguments = new NodeList<>();

        while (tokenIterator.hasNext()){
            arguments.add(ValueParser.parseValue(tokenIterator.next()));
        }

        return new Resources(label, filePath, arguments);
    }
}
