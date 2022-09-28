package lu.uni.serval.ikora.core.runner.convertors;

import lu.uni.serval.ikora.core.runner.Resolved;
import lu.uni.serval.ikora.core.runner.exception.InternalException;
import lu.uni.serval.ikora.core.types.StringType;

public class StringConvertor extends Convertor {
    StringConvertor() {
        super(StringType.class);
    }

    public <T> T convert(Resolved value, Class<T> type) throws InternalException {
        if(type.equals(String.class)){
            return (T)value.getValue();
        }

        throw new InternalException("Cannot convert " + type.getName() + " using boolean convertor.");
    }
}
