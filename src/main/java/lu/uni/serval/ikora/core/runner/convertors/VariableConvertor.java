package lu.uni.serval.ikora.core.runner.convertors;

import lu.uni.serval.ikora.core.runner.Resolved;
import lu.uni.serval.ikora.core.runner.exception.InternalException;
import lu.uni.serval.ikora.core.runner.exception.InvalidArgumentException;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;
import lu.uni.serval.ikora.core.types.VariableType;

import java.util.Collection;
import java.util.List;

public class VariableConvertor extends Convertor {
    VariableConvertor() {
        super(VariableType.class);
    }

    public <T> T convert(List<Resolved> resolvedList, Class<T> type) throws RunnerException {
        if(resolvedList.size() > 1){
            throw new InvalidArgumentException("Boolean convertor should have only one value, but it got " + resolvedList.size() + " instead.");
        }

        final String value = resolvedList.get(0).getOrigin().getName();

        if(type.equals(String.class)){
            return (T) value;
        }

        throw new InternalException("Cannot convert " + type.getName() + " using boolean convertor.");
    }

    @Override
    <C extends Collection<T>, T> C convert(List<Resolved> resolvedList, Class<T> type, Class<C> container) throws RunnerException {
        throw new InternalException("Cannot convert " + container.getName() + " using variable convertor.");
    }
}
