package lu.uni.serval.ikora.core.runner.executors;

import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.runner.Resolved;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.runner.exception.*;
import lu.uni.serval.ikora.core.types.BaseType;
import lu.uni.serval.ikora.core.types.StringType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ArgumentResolver {
    private ArgumentResolver() {}

    public static List<Resolved> resolve(Runtime runtime, Argument argument) throws RunnerException {
        final SourceNode node = argument.getDefinition();

        try{
            return resolve(runtime, argument, node);
        }
        catch (MissingSymbolException e){
            return Collections.singletonList(Resolved.createUnresolved(argument));
        }
    }

    private static List<Resolved> resolve(Runtime runtime, Argument argument, SourceNode node) throws RunnerException {
        if(node instanceof Literal literal){
            return resolve(runtime, argument, literal);
        }
        else if(node instanceof Variable variable){
            return resolve(runtime, argument, variable);
        }
        else if(node instanceof DictionaryEntry entry){
            return resolve(runtime, argument, entry);
        }

        throw new InternalException("Type of node not supported for resolution: " + node.getClass().getName());
    }

    private static List<Resolved> resolve(Runtime runtime, Argument argument, Literal literal) throws RunnerException {
        final List<Variable> variables = literal.getVariables();

        if(variables.isEmpty()){
            final StringType literalType = new StringType("LITERAL");
            literalType.setValue(literal.getName());
            return Collections.singletonList(Resolved.create(literalType, argument));
        }

        String value = literal.getName();

        for(Variable variable: variables){
            final List<Resolved> resolvedVariables = resolve(runtime, argument, variable);

            if(resolvedVariables.size() > 1){
                throw new MalformedVariableException("Composite variable can only accept single value " + variable.getName() + " has " + resolvedVariables.size());
            }

            value = value.replace(variable.getName(), resolvedVariables.get(0).getValue().toString());
        }

        final StringType variableType = new StringType("VARIABLE");
        variableType.setValue(value);
        return Collections.singletonList(Resolved.create(variableType, argument));
    }

    private static List<Resolved> resolve(Runtime runtime, Argument argument, Variable variable) throws RunnerException {
        final Node matching = find(runtime, variable);
        final List<Resolved> resolved = new ArrayList<>();

        if(matching instanceof VariableAssignment variableAssignment){
            final NodeList<Argument> values = variableAssignment.getValues();

            for(Argument value: values){
                try{
                    resolved.addAll(resolve(runtime, value));
                }
                catch (MissingSymbolException e){
                    resolved.add(Resolved.createUnresolved(argument));
                }
            }
        } else if (matching instanceof LibraryVariable libraryVariable) {
            BaseType value = libraryVariable.execute(runtime);
            resolved.add(Resolved.create(value, argument));
        }

        return resolved;
    }

    private static List<Resolved> resolve(Runtime runtime, Argument argument, DictionaryEntry entry) throws RunnerException {
        final Resolved key = getUnique(runtime, argument, entry.getKey());
        final Resolved value = getUnique(runtime, argument, entry.getValue());

        return Collections.singletonList(Resolved.create(key.getKey(), value.getValue(), argument));
    }

    private static Node find(Runtime runtime, Variable variable) throws MultipleSymbolException, MissingSymbolException {
        final Set<Node> matchingSet = runtime.find(variable);

        if(matchingSet.isEmpty()){
            throw new MissingSymbolException("Found no symbol for variable" + variable.getName());
        }

        if(matchingSet.size() > 1){
            throw new MultipleSymbolException("Found more than one symbol for variable" + variable.getName());
        }

        return matchingSet.iterator().next();
    }

    private static Resolved getUnique(Runtime runtime, Argument argument, SourceNode node) throws RunnerException {
        final List<Resolved> valueList = resolve(runtime, argument, node);

        if(valueList.isEmpty()){
            throw new MissingSymbolException("Resolved value for '" + node.getName() + "' is empty.");
        }

        if(valueList.size() > 1){
            throw new MultipleSymbolException("Resolved value for '" + node.getName() + "' should be one but got " + valueList.size() + " instead.");
        }


        return valueList.get(0);
    }
}
