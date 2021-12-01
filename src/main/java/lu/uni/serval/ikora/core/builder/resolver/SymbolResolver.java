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
import lu.uni.serval.ikora.core.runner.Runtime;

public class SymbolResolver {
    private SymbolResolver() {}

    public static void resolve(Runtime runtime) {
        for (SourceFile sourceFile : runtime.getSourceFiles()) {
            for(TestCase testCase: sourceFile.getTestCases()) {
                TestCaseResolver.resolve(testCase, runtime);
            }

            for(UserKeyword userKeyword: sourceFile.getUserKeywords()) {
                UserKeywordResolver.resolve(userKeyword, runtime);
            }

            for(VariableAssignment variableAssignment: sourceFile.getVariables()){
                VariableResolver.resolve(variableAssignment.getVariable(), runtime);

                for(Argument argument: variableAssignment.getValues()){
                    ArgumentResolver.resolve(argument, runtime);
                }
            }
        }
    }
}
