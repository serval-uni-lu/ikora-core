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
package lu.uni.serval.ikora.core.libraries.builtin.keywords;

import lu.uni.serval.ikora.core.libraries.LibraryKeyword;
import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.model.Value;
import lu.uni.serval.ikora.core.parser.ValueParser;
import lu.uni.serval.ikora.core.runner.Resolved;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.ListType;

import java.util.List;
import java.util.stream.Collectors;

public class SetVariable extends LibraryKeyword {
    public SetVariable(){
        super(Type.SET, new ListType("values"));
    }

    @Override
    public void execute(Runtime runtime) {
        final List<Value> values = runtime.getArguments().stream()
                .map(Resolved::getValue)
                .map(v -> ValueParser.parseValue(Token.fromString(v)))
                .collect(Collectors.toList());

        runtime.setReturnValues(values);
    }
}
