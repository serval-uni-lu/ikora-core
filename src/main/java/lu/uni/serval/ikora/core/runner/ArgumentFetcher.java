package lu.uni.serval.ikora.core.runner;

import lu.uni.serval.ikora.core.runner.convertors.TypeConvertor;
import lu.uni.serval.ikora.core.runner.exception.InternalException;
import lu.uni.serval.ikora.core.runner.exception.InvalidTypeException;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;
import lu.uni.serval.ikora.core.types.BaseTypeList;

import java.util.List;
import java.util.Optional;

public class ArgumentFetcher {
    private ArgumentFetcher() {}

    public static <T> T fetch(List<Resolved> arguments, String name, BaseTypeList argumentTypes, Class<T> type) throws RunnerException {
        final int position = argumentTypes.findByName(name);

        if(position == -1){
            throw new InternalException("No name found.");
        }

        final Resolved resolved = getResolved(arguments, argumentTypes, name, position);

        return TypeConvertor.convert(argumentTypes.get(position), resolved, type);
    }

    private static Resolved getResolved(List<Resolved> arguments, BaseTypeList argumentTypes, String name, int position) throws InvalidTypeException {
        final Optional<Resolved> valueByName = findValueByName(name, arguments);

        if(valueByName.isPresent()){
            return valueByName.get();
        }

        if(position < arguments.size()) {
            return arguments.get(position);
        }

        final Optional<String> defaultValue = argumentTypes.get(position).getDefaultValue();

        if(!defaultValue.isPresent()){
            throw new InvalidTypeException("Missing argument and no default provided for " + name);
        }

        return Resolved.create(defaultValue.get());
    }

    private static Optional<Resolved> findValueByName(String name, List<Resolved> arguments){
        return arguments.stream()
                .filter(r -> r.is(name))
                .findFirst();
    }
}
