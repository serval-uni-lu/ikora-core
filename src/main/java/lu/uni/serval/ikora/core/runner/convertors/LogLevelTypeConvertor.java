package lu.uni.serval.ikora.core.runner.convertors;

import lu.uni.serval.ikora.core.model.LogLevel;
import lu.uni.serval.ikora.core.runner.Resolved;
import lu.uni.serval.ikora.core.runner.exception.InternalException;
import lu.uni.serval.ikora.core.runner.exception.InvalidArgumentException;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;
import lu.uni.serval.ikora.core.types.LogLevelType;

import java.util.Collection;
import java.util.List;

public class LogLevelTypeConvertor extends Convertor {
    LogLevelTypeConvertor() {
        super(LogLevelType.class);
    }

    public <T> T convert(List<Resolved> resolvedList, Class<T> type) throws RunnerException {
        if(resolvedList.size() > 1){
            throw new InvalidArgumentException("Boolean convertor should have only one value, but it got " + resolvedList.size() + " instead.");
        }

        final String value = resolvedList.get(0).getValue();

        if(type.equals(LogLevel.class)){
            return (T)LogLevel.valueOf(value.toUpperCase());
        }

        throw new InternalException("Cannot convert " + type.getName() + " using boolean convertor.");
    }

    @Override
    <C extends Collection<T>, T> C convert(List<Resolved> resolvedList, Class<T> type, Class<C> container) throws RunnerException {
        throw new InternalException("Cannot convert " + container.getName() + " using boolean convertor.");
    }
}
