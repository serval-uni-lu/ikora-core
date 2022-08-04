package lu.uni.serval.ikora.core.builder.resolver;

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

import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.utils.Ast;
import lu.uni.serval.ikora.core.runtime.Runtime;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class VariableResolver {
    private VariableResolver() {}

    public static void resolve(Variable variable, Runtime runtime){
        final Set<Dependable> definitions = new HashSet<>();

        Optional<ScopeNode> parentScope = Ast.getParentByType(variable, ScopeNode.class);
        while(parentScope.isPresent()){
            definitions.addAll(parentScope.get().findDefinition(variable));
            if(!definitions.isEmpty()) break;

            parentScope = Ast.getParentByType(parentScope.get(), ScopeNode.class);
        }

        if(definitions.isEmpty()){
            final SourceFile sourceFile = variable.getSourceFile();
            definitions.addAll(sourceFile.findVariable(variable.getDefinitionToken()));
        }

        if(definitions.isEmpty()){
            runtime.findLibraryVariable(variable.getDefinitionToken()).ifPresent(definitions::add);
        }

        definitions.forEach(d -> variable.linkToDefinition(d, Link.Import.STATIC));
    }
}
