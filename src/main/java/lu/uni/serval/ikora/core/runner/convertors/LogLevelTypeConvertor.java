package lu.uni.serval.ikora.core.runner.convertors;

import lu.uni.serval.ikora.core.model.LogLevel;
import lu.uni.serval.ikora.core.runner.exception.InternalException;

public class LogLevelTypeConvertor {
    private LogLevelTypeConvertor() {}

    public static <T> T convert(String value, Class<T> type) throws InternalException {
        if(type.equals(LogLevel.class)){
            return (T)LogLevel.valueOf(value.toUpperCase());
        }

        throw new InternalException("Cannot convert " + type.getName() + " using boolean convertor.");
    }
}
