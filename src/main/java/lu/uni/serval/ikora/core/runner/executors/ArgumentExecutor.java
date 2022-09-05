package lu.uni.serval.ikora.core.runner.executors;

import lu.uni.serval.ikora.core.model.Argument;
import lu.uni.serval.ikora.core.model.NodeList;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;
import lu.uni.serval.ikora.core.types.BaseType;
import lu.uni.serval.ikora.core.types.BaseTypeList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArgumentExecutor extends NodeExecutor {
    private final BaseTypeList argumentTypes;
    private final NodeList<Argument> arguments;

    public ArgumentExecutor(Runtime runtime, BaseTypeList argumentTypes, NodeList<Argument> arguments) {
        super(runtime, arguments);
        this.argumentTypes = argumentTypes;
        this.arguments = arguments;
    }

    @Override
    protected void executeImpl() throws RunnerException {
        final List<Argument> resolved = resolveArgument();
        validateArguments(resolved);
        registerArguments(resolved);
    }

    private List<Argument> resolveArgument() {
        return new ArrayList<>(this.arguments.toList());
    }

    private void validateArguments(List<Argument> resolved) throws RunnerException {
        final Iterator<BaseType> typeIt = argumentTypes.iterator();
        final Iterator<Argument> argumentIt = resolved.iterator();

        while(typeIt.hasNext()){
            final BaseType type = typeIt.next();

            if(argumentIt.hasNext()){
                validate(type, argumentIt);
            }
            else {
                if(!type.hasDefaultValue()){
                    throw new RunnerException("Missing argument");
                }
            }
        }

        if(argumentIt.hasNext()){
            throw new RunnerException("Too many argument passed");
        }
    }

    private void validate(BaseType type, Iterator<Argument> argumentIt) throws RunnerException {
        while(argumentIt.hasNext()){
            final Argument argument = argumentIt.next();

            if(!type.isValid(argument)){
                throw new RunnerException("Invalid type");
            }

            if(!type.isSingleValue()) break;
        }
    }

    private void registerArguments(List<Argument> resolved){
        runtime.addArgumentToScope(resolved);
    }
}
