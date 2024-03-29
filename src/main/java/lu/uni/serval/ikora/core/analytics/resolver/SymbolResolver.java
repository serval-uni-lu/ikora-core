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
package lu.uni.serval.ikora.core.analytics.resolver;

import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.model.*;

public class SymbolResolver {
    private SymbolResolver() {}

    public static void resolve(Project project, StaticScope staticScope, ErrorManager errorManager) {
        for (SourceFile sourceFile : project.getSourceFiles()) {
            for(TestCase testCase: sourceFile.getTestCases()) {
                TestCaseResolver.resolve(staticScope, testCase, errorManager);
            }

            for(UserKeyword userKeyword: sourceFile.getUserKeywords()) {
                UserKeywordResolver.resolve(staticScope, userKeyword, errorManager);
            }

            for(VariableAssignment variableAssignment: sourceFile.getVariables()){
                for(Argument argument: variableAssignment.getValues()){
                    ArgumentResolver.resolve(staticScope, argument);
                }
            }
        }
    }
}
