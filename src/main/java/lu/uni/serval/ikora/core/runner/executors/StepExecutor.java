/*
 *
 *     Copyright © 2022 University of Luxembourg
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
package lu.uni.serval.ikora.core.runner.executors;

import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.utils.Finder;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;

import java.util.*;

public class StepExecutor extends NodeExecutor {
    private final Step step;

    StepExecutor(Runtime runtime, Step step) {
        super(runtime, step);
        this.step = step;
    }

    @Override
    protected void executeImpl() throws RunnerException {
        if(step instanceof KeywordCall call){
            execute(call);
        }
        else if(step instanceof Assignment assignment){
            execute(assignment);
        }
    }

    private void execute(KeywordCall call) throws RunnerException {
        final Set<Keyword> keywords = Finder.findKeywords(runtime.getLibraryResources(), call);

        if(keywords.isEmpty()){
            runtime.registerSymbolErrorAndThrow(call.getSource(), "No definition found", call.getRange());
        }

        if(keywords.size() > 1){
            runtime.registerSymbolErrorAndThrow(call.getSource(), "Found multiple definition", call.getRange());
        }

        final Keyword keyword = keywords.iterator().next();

        new ArgumentExecutor(runtime, keyword.getArgumentTypes(), call.getArgumentList()).execute();
        new KeywordExecutor(runtime, keyword).execute();
    }

    private void execute(Assignment assignment) throws RunnerException {
        final Optional<KeywordCall> call = assignment.getKeywordCall();

        if(call.isPresent()){
            execute(call.get());
            final List<Value> returnValues = runtime.getReturnValues();
            final List<Variable> variables = assignment.getLeftHandOperand();
            final List<VariableAssignment> variableAssignments = assignValuesToVariables(variables, returnValues);

            for(VariableAssignment variableAssignment: variableAssignments){
                runtime.addToKeywordScope(variableAssignment);
            }
        }
    }

    private List<VariableAssignment> assignValuesToVariables(List<Variable> variables, List<Value> values){
        if(values.isEmpty()){
            return Collections.emptyList();
        }

        final List<VariableAssignment> assignments = new ArrayList<>(variables.size());
        final Iterator<Value> valueIt = values.iterator();
        for(Variable variable: variables){
            final VariableAssignment assignment = new VariableAssignment(variable);
            assignments.add(assignment);

            do{
                assignment.addValue(valueIt.next());
            }
            while (valueIt.hasNext() && !(variable instanceof ScalarVariable));
        }

        return assignments;
    }
}
