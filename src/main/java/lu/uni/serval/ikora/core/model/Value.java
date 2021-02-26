package lu.uni.serval.ikora.core.model;

import java.util.Optional;

public abstract class Value extends SourceNode {
    public static int findFirst(NodeList<Value> values, Class<?> type){
        final Optional<Value> first = values.stream()
                .filter(a -> type.isAssignableFrom(a.getClass()))
                .findFirst();

        return first.map(values::indexOf).orElse(-1);
    }
}
