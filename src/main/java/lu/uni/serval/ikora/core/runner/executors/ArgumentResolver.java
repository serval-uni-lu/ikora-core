package lu.uni.serval.ikora.core.runner.executors;

import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.runner.Resolved;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.runner.exception.MalformedVariableException;
import lu.uni.serval.ikora.core.runner.exception.MissingSymbolException;
import lu.uni.serval.ikora.core.runner.exception.MultipleSymbolException;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ArgumentResolver {
    private ArgumentResolver() {}

    public static List<Resolved> resolve(Runtime runtime, Argument argument) throws RunnerException {
        final SourceNode node = argument.getDefinition();
        return resolve(runtime, argument, node);
    }

    private static List<Resolved> resolve(Runtime runtime, Argument argument, SourceNode node) throws RunnerException {
        if(node instanceof Literal){
            return resolve(runtime, argument, (Literal)node);
        }
        else if(node instanceof ScalarVariable){
            return resolve(runtime, argument, (ScalarVariable)node);
        }

        return resolve(runtime, argument, (Variable) node);
    }

    private static List<Resolved> resolve(Runtime runtime, Argument argument, Literal literal) throws RunnerException {
        final List<Variable> variables = literal.getVariables();

        if(variables.isEmpty()){
            return Collections.singletonList(Resolved.create(literal.getName(), argument));
        }

        String value = literal.getName();

        for(Variable variable: variables){
            final List<Resolved> resolvedVariables = resolve(runtime, argument, variable);

            if(resolvedVariables.size() > 1){
                throw new MalformedVariableException("Composite variable can only accept single value " + variable.getName() + " has " + resolvedVariables.size());
            }

            value = value.replace(variable.getName(), resolvedVariables.get(0).getValue());
        }

        return Collections.singletonList(Resolved.create(value, argument));
    }

    private static List<Resolved> resolve(Runtime runtime, Argument argument, Variable variable) throws RunnerException {
        final Set<Node> matchingSet = runtime.find(variable);

        if(matchingSet.isEmpty()){
            throw new MissingSymbolException("Could not resolve variable " + variable.getName());
        }

        if(matchingSet.size() > 1){
            throw new MultipleSymbolException("Found more than one symbol for variable" + variable.getName());
        }

        final Node matching = matchingSet.iterator().next();

        final List<Resolved> resolved = new ArrayList<>();

        if(matching instanceof VariableAssignment){
            final NodeList<Argument> values = ((VariableAssignment) matching).getValues();

            for(Argument value: values){
                resolved.addAll(resolve(runtime, value));
            }
        }

        return resolved;
    }
}
