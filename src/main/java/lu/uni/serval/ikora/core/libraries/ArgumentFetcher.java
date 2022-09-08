package lu.uni.serval.ikora.core.libraries;

import lu.uni.serval.ikora.core.runner.Resolved;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.runner.convertors.TypeConvertor;
import lu.uni.serval.ikora.core.runner.exception.InternalException;
import lu.uni.serval.ikora.core.runner.exception.InvalidTypeException;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;

import java.util.List;
import java.util.Optional;

public class ArgumentFetcher {
    private ArgumentFetcher() {}

    public static <T> T fetch(Runtime runtime, String name, LibraryKeyword keyword, Class<T> type) throws RunnerException {
        final int position = keyword.getArgumentTypes().findByName(name);

        if(position == -1){
            throw new InternalException("No name found.");
        }

        final Resolved resolved = getResolved(keyword, name, position, runtime);

        return TypeConvertor.convert(keyword.getArgumentTypes().get(position), resolved.getValue(), runtime, type);
    }

    private static Resolved getResolved(LibraryKeyword keyword, String name, int position, Runtime runtime) throws InvalidTypeException {
        final List<Resolved> arguments = runtime.getArguments();
        final Optional<Resolved> valueByName = findValueByName(name, arguments);

        if(valueByName.isPresent()){
            return valueByName.get();
        }

        if(position < arguments.size()) {
            return arguments.get(position);
        }

        final Optional<String> defaultValue = keyword.getArgumentTypes().get(position).getDefaultValue();

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
