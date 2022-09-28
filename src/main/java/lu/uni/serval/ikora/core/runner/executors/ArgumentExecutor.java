package lu.uni.serval.ikora.core.runner.executors;

import lu.uni.serval.ikora.core.model.Argument;
import lu.uni.serval.ikora.core.model.NodeList;
import lu.uni.serval.ikora.core.runner.Resolved;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.runner.exception.InternalException;
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
        final List<Resolved> resolved = new ArrayList<>(this.arguments.size());
        resolveArgument(resolved);
        validateArguments(resolved);
        registerArguments(resolved);
    }

    private void registerArguments(List<Resolved> resolved) throws InternalException {
        runtime.addArgumentToScope(resolved);
    }

    private void resolveArgument(List<Resolved> resolved) throws RunnerException {
        for(Argument argument: arguments){
            resolved.addAll(ArgumentResolver.resolve(runtime, argument));
        }
    }

    private void validateArguments(List<Resolved> resolved) throws RunnerException {
        final Iterator<BaseType> typeIt = argumentTypes.iterator();
        final Iterator<Resolved> resolvedIt = resolved.iterator();

        while(typeIt.hasNext()){
            final BaseType type = typeIt.next();

            if(resolvedIt.hasNext()){
                validateArgument(resolvedIt, type);
            }
            else if(!type.hasDefaultValue()){
                throw new RunnerException("Missing argument");
            }
        }

        if(resolvedIt.hasNext()){
            throw new RunnerException("Too many argument passed");
        }
    }

    private void validateArgument(Iterator<Resolved> resolvedIt, BaseType type) throws RunnerException {
        do{
            final Resolved current = resolvedIt.next();

            if(!type.isValid(current)){
                throw new RunnerException("Invalid argument for type " + type.getClass().getName() + ": " + current.getValue());
            }

            if(type.isSingleValue()) break;
        }
        while(resolvedIt.hasNext());
    }
}
