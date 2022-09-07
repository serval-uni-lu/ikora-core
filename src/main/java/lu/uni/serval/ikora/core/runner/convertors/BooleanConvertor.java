package lu.uni.serval.ikora.core.runner.convertors;

import lu.uni.serval.ikora.core.runner.exception.InternalException;
import lu.uni.serval.ikora.core.runner.exception.InvalidTypeException;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;
import lu.uni.serval.ikora.core.types.BooleanType;

public class BooleanConvertor {
    private BooleanConvertor() {}

    public static <T> T convert(String value, Class<T> type) throws RunnerException {
        if(type == Boolean.class){
            return (T)toBoolean(value);
        }

        if(type == String.class){
            return (T)value;
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
