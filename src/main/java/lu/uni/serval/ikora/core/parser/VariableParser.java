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
import lu.uni.serval.ikora.core.model.*;

import java.util.Optional;

public class VariableParser {
    private VariableParser(){}

    public static Optional<Variable> parse(final Token name){
        if(!ValueResolver.isVariable(name)){
            return Optional.empty();
        }

        Variable variable;

        switch (name.getText().substring(0, 1)) {
            case "$":  variable = new ScalarVariable(name); break;
            case "@":  variable = new ListVariable(name); break;
            case "&": variable = new DictionaryVariable(name); break;
            default: variable = null;
        }

        if(variable != null){
            variable.addToken(name);
        }

        return Optional.ofNullable(variable);
    }
}
