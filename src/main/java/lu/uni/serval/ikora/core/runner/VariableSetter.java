package lu.uni.serval.ikora.core.runner;

import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.parser.VariableParser;
import lu.uni.serval.ikora.core.runner.exception.InvalidArgumentException;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;
import lu.uni.serval.ikora.core.types.BaseTypeList;

import java.util.List;
import java.util.Optional;

import static lu.uni.serval.ikora.core.runner.ArgumentFetcher.fetch;

public class VariableSetter {
    private VariableSetter() {}
    public static VariableAssignment fromArguments(List<Resolved> arguments, BaseTypeList argumentTypes) throws RunnerException {
        final String name = fetch(arguments, "name", argumentTypes, String.class);
        final Optional<Variable> parse = VariableParser.parse(Token.fromString(name));

        if(!parse.isPresent()){
            throw new InvalidArgumentException("Should be a variable but got " + name + " instead");
        }

        final Variable variable = parse.get();

        if((variable instanceof ScalarVariable) && arguments.size() != 2){
            throw new InvalidArgumentException("Should have 1 value assigned to Scalar Variable but got " + (arguments.size() - 1));
        }

        final VariableAssignment variableAssignment = new VariableAssignment(variable);
        final List<Value> values = fetch(arguments, "values", argumentTypes, Value.class, List.class);
        values.forEach(variableAssignment::addValue);

        return variableAssignment;
    }
}
