package lu.uni.serval.ikora.core.types;

import lu.uni.serval.ikora.core.runner.exception.InvalidTypeException;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class NullType extends BaseType {
    public NullType(String name) {
        super(name, false);
    }

    @Override
    public boolean isSingleValue() {
        return true;
    }

    @Override
    public Optional<String> asString() {
        throw new NotImplementedException();
    }

    @Override
    public <T> T convert(List<BaseType> from, Class<T> to) throws InvalidTypeException {
        throw new NotImplementedException();
    }

    @Override
    public <C extends Collection<T>, T> C convert(List<BaseType> from, Class<T> to, Class<C> container) throws InvalidTypeException {
        throw new NotImplementedException();
    }
}
