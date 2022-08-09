package lu.uni.serval.ikora.core.libraries.builtin.keywords;

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

import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.types.ListType;
import lu.uni.serval.ikora.core.types.StringType;
import lu.uni.serval.ikora.core.utils.Ast;

import java.util.Optional;

public class ImportLibrary extends LibraryKeyword implements ScopeModifier {
    public ImportLibrary(){
        super(Type.CONFIGURATION,
                new StringType("name"),
                new ListType("args")
        );
    }

    @Override
    public void modifyScope(ScopeManager scopeManager, KeywordCall call, ErrorManager errorManager) {
        Optional<KeywordDefinition> parent = Ast.getParentByType(call, KeywordDefinition.class);

        if(parent.isPresent()){
            scopeManager.addLibraryToScope(parent.get(), call.getArgumentList());
        }
        else{
            errorManager.registerSymbolError(
                    call.getSource(),
                    "Failed to resolve dynamic import",
                    call.getRange()
            );
        }
    }
}
