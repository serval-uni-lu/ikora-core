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

import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.libraries.LibraryKeyword;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.runner.VariableSetter;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;
import lu.uni.serval.ikora.core.types.ListType;
import lu.uni.serval.ikora.core.types.StringType;

import java.util.List;

public class SetGlobalVariable extends LibraryKeyword implements ScopeModifier {
    public SetGlobalVariable(){
        super(Type.SET,
                new StringType("name"),
                new ListType("values")
        );
    }

    @Override
    public void execute(Runtime runtime) throws RunnerException {
        final VariableAssignment scopedVariable = VariableSetter.fromArguments(runtime.getArguments(), argumentTypes);
        runtime.addToGlobalScope(scopedVariable);
    }

    @Override
    public void modifyScope(ScopeManager scopeManager, KeywordCall call, ErrorManager errorManager) {
        List<Argument> argumentList = call.getArgumentList();

        if(argumentList.size() < 2){
            errorManager.registerInternalError(
                    call.getSource(),
                    "Failed to update global scope: not enough arguments found. Need 2!",
                    call.getRange()
            );
        }
        else{
            try {
                Variable variable = Variable.create(argumentList.get(0).getDefinitionToken());
                scopeManager.addToGlobalScope(variable);
            } catch (Exception e) {
                errorManager.registerInternalError(
                        call.getSource(),
                        "Failed to update global scope: malformed variable.",
                        call.getRange()
                );
            }
        }
    }
}
