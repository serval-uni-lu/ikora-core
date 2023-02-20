package lu.uni.serval.ikora.core.runner;

import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.runner.exception.InvalidArgumentException;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;
import lu.uni.serval.ikora.core.types.BaseTypeList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static lu.uni.serval.ikora.core.runner.ArgumentFetcher.fetch;

public class VariableSetter {
    private VariableSetter() {}
    public static VariableAssignment fromArguments(List<Resolved> arguments, BaseTypeList argumentTypes) throws RunnerException {
        final Variable variable = fetch(arguments, "name", argumentTypes, Variable.class);
        final List<Value> values = getValues(variable, arguments.subList(1, arguments.size()));
        final VariableAssignment variableAssignment = new VariableAssignment(variable);
        values.forEach(variableAssignment::addValue);

        return variableAssignment;
    }

    private static List<Value> getValues(Variable variable, List<Resolved> resolvedList) throws RunnerException {
        if(variable instanceof ScalarVariable){
            return getScalarValue(resolvedList);
        }

        if(variable instanceof DictionaryVariable){
            return getDictionaryValues(resolvedList);
        }

        if (variable instanceof ListVariable){
            return getListValues(resolvedList);
        }

        throw new RunnerException(String.format(
                "Unsupported variable type: %s", variable.getClass().getCanonicalName()
        ));
    }

    private static List<Value> getScalarValue(List<Resolved> resolvedList) throws InvalidArgumentException {
        if(resolvedList.size() != 1){
            throw new InvalidArgumentException(String.format(
                    "Should have 1 value assigned to Scalar Variable but got: %s",
                    resolvedList.size()
            ));
        }

        Optional<String> literal = resolvedList.get(0).getValue().asString();

        if(literal.isEmpty()){
            throw new InvalidArgumentException("Value assigned to Scalar Variable is empty.");
        }

        return Collections.singletonList(new Literal(literal.get()));
    }

    private static List<Value> getDictionaryValues(List<Resolved> resolvedList) throws InvalidArgumentException {
        final List<Value> values = new ArrayList<>(resolvedList.size());

        for(Resolved resolved: resolvedList){
            final Literal key = new Literal(resolved.getKey());
            final Optional<String> valueString = resolved.getValue().asString();

            if(valueString.isEmpty()){
                throw new InvalidArgumentException(String.format(
                        "Value assigned to Dictionary Entry with key '%s' is missing.", key
                ));
            }

            values.add(new DictionaryEntry(key, new Literal(valueString.get())));
        }

        return values;
    }

    private static List<Value> getListValues(List<Resolved> resolvedList) throws InvalidArgumentException {
        final List<Value> values = new ArrayList<>(resolvedList.size());

        for(Resolved resolved: resolvedList){
            final Optional<String> valueString = resolved.getValue().asString();

            if(valueString.isEmpty()){
                throw new InvalidArgumentException("Value assigned to List Variable is missing.");
            }

            values.add(new Literal(valueString.get()));
        }

        return values;
    }
}
