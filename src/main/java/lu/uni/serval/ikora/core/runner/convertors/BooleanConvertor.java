package lu.uni.serval.ikora.core.runner.convertors;

import lu.uni.serval.ikora.core.runner.Resolved;
import lu.uni.serval.ikora.core.runner.exception.InternalException;
import lu.uni.serval.ikora.core.runner.exception.InvalidTypeException;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;
import lu.uni.serval.ikora.core.types.BooleanType;

public class BooleanConvertor extends Convertor{
    public BooleanConvertor(){
        super(BooleanType.class);
    }

    public <T> T convert(Resolved value, Class<T> type) throws RunnerException {
        if(type == Boolean.class){
            return (T)toBoolean(value.getValue());
        }

        if(type == String.class){
            return (T)value.getValue();
        }

        throw new InternalException("Cannot convert " + type.getName() + " using boolean convertor.");
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
