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

import lu.uni.serval.ikora.core.analytics.visitor.FindTestCaseVisitor;
import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.libraries.LibraryKeyword;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.parser.VariableParser;
import lu.uni.serval.ikora.core.runner.Resolved;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.runner.exception.InvalidArgumentException;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;
import lu.uni.serval.ikora.core.types.ListType;
import lu.uni.serval.ikora.core.types.VariableType;

import java.util.List;
import java.util.Optional;

import static lu.uni.serval.ikora.core.runner.ArgumentFetcher.fetch;

public class SetTestVariable extends LibraryKeyword implements ScopeModifier {
    public SetTestVariable(){
        super(Type.SET,
                new VariableType("name"),
                new ListType("values")
        );
    }

    @Override
    public void execute(Runtime runtime) throws RunnerException {
        final List<Resolved> arguments = runtime.getArguments();

        final String name = fetch(arguments, "name", argumentTypes, String.class);
        final Optional<Variable> parse = VariableParser.parse(Token.fromString(name));

        if(!parse.isPresent()){
            throw new InvalidArgumentException("Should be a variable but got " + name + " instead");
        }

        final Variable variable = parse.get();

        if(variable instanceof ScalarVariable){
            if(arguments.size() != 2){
                throw new InvalidArgumentException("Should have 1 value assigned to Scalar Variable but got " + (arguments.size() - 1));
            }

            final VariableAssignment variableAssignment = new VariableAssignment(variable);
            final Value value = fetch(arguments, "values", argumentTypes, Value.class);
            variableAssignment.addValue(value);

            runtime.addToTestScope(variableAssignment);
        }
    }
    @Override
    public void modifyScope(ScopeManager manager, KeywordCall call, ErrorManager errorManager) {
        NodeList<Argument> argumentList = call.getArgumentList();

        if(argumentList.size() < 2){
            errorManager.registerInternalError(
                    call.getSource(),
                    "Failed to update test scope: no argument found.",
                    call.getRange()
            );
        }
        else{
            try {
                Variable variable = Variable.create(argumentList.get(0).getDefinitionToken());

                FindTestCaseVisitor visitor = new FindTestCaseVisitor();
                call.accept(visitor, new PathMemory());

                for(TestCase testCase: visitor.getTestCases()){
                    manager.addToTestScope(testCase, variable);
                }
            } catch (Exception e) {
                errorManager.registerInternalError(
                        call.getSource(),
                        "Failed to update test scope: malformed variable.",
                        call.getRange()
                );
            }
        }
    }
}
