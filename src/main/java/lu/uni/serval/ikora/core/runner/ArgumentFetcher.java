package lu.uni.serval.ikora.core.runner;

import lu.uni.serval.ikora.core.runner.convertors.TypeConvertor;
import lu.uni.serval.ikora.core.runner.exception.InternalException;
import lu.uni.serval.ikora.core.runner.exception.InvalidTypeException;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;
import lu.uni.serval.ikora.core.types.BaseTypeList;

import java.util.*;

public class ArgumentFetcher {
    private ArgumentFetcher() {}

    public static <T> T fetch(List<Resolved> arguments, String name, BaseTypeList argumentTypes, Class<T> type) throws RunnerException {
        final int position = argumentTypes.findByName(name);

        if(position == -1){
            throw new InternalException("No name found.");
        }

        final List<Resolved> resolved = getResolved(arguments, argumentTypes, name, position);

        return TypeConvertor.convert(argumentTypes.get(position), resolved, type);
    }

    public static <C extends Collection<T>, T> C fetch(List<Resolved> arguments, String name, BaseTypeList argumentTypes, Class<T> type, Class<C> container) throws RunnerException {
        final int position = argumentTypes.findByName(name);

        if(position == -1){
            throw new InternalException("No name found.");
        }

        final List<Resolved> resolved = getResolved(arguments, argumentTypes, name, position);

        return TypeConvertor.convert(argumentTypes.get(position), resolved, type, container);
    }

    private static List<Resolved> getResolved(List<Resolved> arguments, BaseTypeList argumentTypes, String name, int position) throws InvalidTypeException {
        final Optional<Resolved> valueByName = findValueByName(name, arguments);

        if(valueByName.isPresent()){
            return Collections.singletonList(valueByName.get());
        }

        if(position < arguments.size()) {
            if(argumentTypes.get(position).isSingleValue()){
                return Collections.singletonList(arguments.get(position));
            }

            final List<Resolved> resolvedList = new ArrayList<>();

            while(position < arguments.size() && arguments.get(position).getKey().isEmpty()){
                resolvedList.add(arguments.get(position));
                ++position;
            }

            return resolvedList;
        }

        final Optional<String> defaultValue = argumentTypes.get(position).getDefaultValue();

        if(!defaultValue.isPresent()){
            throw new InvalidTypeException("Missing argument and no default provided for " + name);
        }

        return Collections.singletonList(Resolved.create(defaultValue.get()));
    }

    private static Optional<Resolved> findValueByName(String name, List<Resolved> arguments){
        return arguments.stream()
                .filter(r -> r.is(name))
                .findFirst();
    }
}
