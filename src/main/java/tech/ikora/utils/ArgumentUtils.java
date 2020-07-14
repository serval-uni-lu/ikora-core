package tech.ikora.utils;

import tech.ikora.model.*;

import java.util.Optional;

public class ArgumentUtils {
    public static int findFirst(NodeList<Argument> arguments, Class<?> type){
        final Optional<Argument> first = arguments.stream()
                .filter(a -> a.isType(type))
                .findFirst();

        return first.map(arguments::indexOf).orElse(-1);
    }

    public static boolean isExpendedUntilPosition(NodeList<Argument> arguments, int position){
        int listIndex = findFirst(arguments, ListVariable.class);
        int dictIndex = findFirst(arguments, DictionaryVariable.class);

        int varIndex = arguments.size();
        varIndex = listIndex != -1 ? Math.min(varIndex, listIndex) : varIndex;
        varIndex = dictIndex != -1 ? Math.min(varIndex, dictIndex) : varIndex;

        return position < varIndex;
    }

    public static boolean contains(NodeList<Argument> arguments, Value value){
        return arguments.stream().map(Argument::getDefinition).anyMatch(v -> v == value);
    }
}
