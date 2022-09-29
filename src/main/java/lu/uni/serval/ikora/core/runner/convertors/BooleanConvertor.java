package lu.uni.serval.ikora.core.runner.convertors;

import lu.uni.serval.ikora.core.runner.Resolved;
import lu.uni.serval.ikora.core.runner.exception.InternalException;
import lu.uni.serval.ikora.core.runner.exception.InvalidArgumentException;
import lu.uni.serval.ikora.core.runner.exception.InvalidTypeException;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;
import lu.uni.serval.ikora.core.types.BooleanType;

import java.util.Collection;
import java.util.List;

public class BooleanConvertor extends Convertor{
    public BooleanConvertor(){
        super(BooleanType.class);
    }

    public <T> T convert(List<Resolved> resolvedList, Class<T> type) throws RunnerException {

        if(resolvedList.size() > 1){
            throw new InvalidArgumentException("Boolean convertor should have only one value, but it got " + resolvedList.size() + " instead.");
        }

        final String value = resolvedList.get(0).getValue();

        if(type == Boolean.class){
            return (T)toBoolean(value);
        }

        if(type == String.class){
            return (T)value;
        }

        throw new InternalException("Cannot convert " + type.getName() + " using boolean convertor.");
    }

    @Override
    <C extends Collection<T>, T> C convert(List<Resolved> resolvedList, Class<T> type, Class<C> container) throws RunnerException {
        throw new InternalException("Cannot convert " + container.getName() + " using boolean convertor.");
    }

    private static Boolean toBoolean(String value) throws InvalidTypeException {
        if(BooleanType.FALSE.equalsIgnoreCase(value)){
            return false;
        }

        if(BooleanType.TRUE.equalsIgnoreCase(value)){
            return true;
        }

        throw new InvalidTypeException("Invalid boolean value: " + value);
    }
}
