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

import lu.uni.serval.ikora.core.analytics.resolver.ValueResolver;
import lu.uni.serval.ikora.core.model.Literal;
import lu.uni.serval.ikora.core.model.NodeList;
import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.model.Variable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LiteralParser {
    private LiteralParser() {}

    public static Literal parse(Token token){
        final List<Token> variableNames = ValueResolver.findVariables(token);

        final List<Variable> variables = variableNames.stream().map(VariableParser::parse)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return new Literal(token, new NodeList<>(variables));
    }
}
