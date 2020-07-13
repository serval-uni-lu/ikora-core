package tech.ikora.model;

import java.util.Optional;

public abstract class Value extends SourceNode {
    public static int findFirst(NodeList<Value> values, Class<?> type){
        final Optional<Value> first = values.stream()
                .filter(a -> type.isAssignableFrom(a.getClass()))
                .findFirst();

        return first.map(values::indexOf).orElse(-1);
    }

    public boolean isExpendedUntilPosition(NodeList<Value> values, int position){
        int listIndex = findFirst(values, ListVariable.class);
        int dictIndex = findFirst(values, DictionaryVariable.class);

        int varIndex = values.size();
        varIndex = listIndex != -1 ? Math.min(varIndex, listIndex) : varIndex;
        varIndex = dictIndex != -1 ? Math.min(varIndex, dictIndex) : varIndex;

        return position < varIndex;
    }
}
