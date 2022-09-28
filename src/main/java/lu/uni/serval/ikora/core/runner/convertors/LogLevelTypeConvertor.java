package lu.uni.serval.ikora.core.runner.convertors;

import lu.uni.serval.ikora.core.model.LogLevel;
import lu.uni.serval.ikora.core.runner.Resolved;
import lu.uni.serval.ikora.core.runner.exception.InternalException;
import lu.uni.serval.ikora.core.types.LogLevelType;

public class LogLevelTypeConvertor extends Convertor {
    LogLevelTypeConvertor() {
        super(LogLevelType.class);
    }

    public <T> T convert(Resolved value, Class<T> type) throws InternalException {
        if(type.equals(LogLevel.class)){
            return (T)LogLevel.valueOf(value.getValue().toUpperCase());
        }

        throw new InternalException("Cannot convert " + type.getName() + " using boolean convertor.");
    }
}
