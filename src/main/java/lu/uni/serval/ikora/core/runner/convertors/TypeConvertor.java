package lu.uni.serval.ikora.core.runner.convertors;

import lu.uni.serval.ikora.core.runner.exception.InvalidTypeException;
import lu.uni.serval.ikora.core.runner.Resolved;
import lu.uni.serval.ikora.core.types.*;

import java.util.Collection;
import java.util.List;

public class TypeConvertor {

    private TypeConvertor() {}

    public static <T> T convert(BaseType baseType, List<Resolved> value, Class<T> type) throws InvalidTypeException {
        final List<BaseType> values = value.stream().map(Resolved::getValue).toList();
        return baseType.convert(values, type);
    }

    public static <C extends Collection<T>, T> C convert(BaseType baseType, List<Resolved> value, Class<T> type, Class<C> container) throws InvalidTypeException {
        final List<BaseType> values = value.stream().map(Resolved::getValue).toList();
        return baseType.convert(values, type, container);
    }
}
