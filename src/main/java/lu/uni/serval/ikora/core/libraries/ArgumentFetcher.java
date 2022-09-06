package lu.uni.serval.ikora.core.libraries;

import lu.uni.serval.ikora.core.model.Argument;
import lu.uni.serval.ikora.core.model.DictionaryEntry;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.runner.exception.InternalException;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;

import java.util.List;
import java.util.Optional;


public class ArgumentFetcher {
    public static <T> T fetch(String name, LibraryKeyword keyword, Runtime runtime) throws RunnerException {
        final int position = keyword.getArgumentTypes().findByName(name);

        if(position == -1){
            throw new InternalException("No name found.");
        }

        final List<Argument> arguments = runtime.getArguments();
        final Optional<Argument> argumentByName = findArgumentByName(name, arguments);

//        if(argumentByName.isPresent()){
//            return keyword.getArgumentTypes().get(position).convert(argumentByName.get().ge);
//        }

        return null;
    }

    private static Optional<Argument> findArgumentByName(String name, List<Argument> arguments){
        return arguments.stream()
                .filter(a -> a.isDictionaryEntry()
                        && ((DictionaryEntry)a.getDefinition()).getKey().getName().equalsIgnoreCase(name))
                .findFirst();
    }
}
