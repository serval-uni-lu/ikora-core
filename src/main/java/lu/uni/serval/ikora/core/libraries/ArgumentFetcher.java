package lu.uni.serval.ikora.core.libraries;

import lu.uni.serval.ikora.core.model.Argument;
import lu.uni.serval.ikora.core.model.DictionaryEntry;
import lu.uni.serval.ikora.core.model.SourceNode;
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

        final String value = getValue(keyword, name, position, runtime);

        return TypeConvertor.convert(keyword.getArgumentTypes().get(position), value, runtime, type);
    }

    private static String getValue(LibraryKeyword keyword, String name, int position, Runtime runtime) throws InvalidTypeException {
        final List<Argument> arguments = runtime.getArguments();
        final Optional<SourceNode> argumentByName = findArgumentByName(name, arguments);

        if(argumentByName.isPresent()){
            final SourceNode node = argumentByName.get();
            return node.getName();
        }

        if(position < arguments.size()) {
            final SourceNode node = arguments.get(position).getDefinition();
            return node.getName();
        }

        final Optional<String> defaultValue = keyword.getArgumentTypes().get(position).getDefaultValue();

        if(!defaultValue.isPresent()){
            throw new InvalidTypeException("Missing argument and no default provided for " + name);
        }

        return defaultValue.get();
    }

    private static Optional<SourceNode> findArgumentByName(String name, List<Argument> arguments){
        return arguments.stream()
                .filter(Argument::isDictionaryEntry)
                .map(Argument::getDefinition)
                .map(DictionaryEntry.class::cast)
                .filter(e -> e.getKey().getName().equalsIgnoreCase(name))
                .map(DictionaryEntry::getValue)
                .findFirst();
    }
}
