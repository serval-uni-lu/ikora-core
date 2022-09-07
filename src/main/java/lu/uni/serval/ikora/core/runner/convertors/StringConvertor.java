package lu.uni.serval.ikora.core.runner.convertors;

import lu.uni.serval.ikora.core.runner.exception.InternalException;

public class StringConvertor {
    private StringConvertor() {}

    public static <T> T convert(String value, Class<T> type) throws InternalException {
        if(type.equals(String.class)){
            return (T)value;
        }

        throw new InternalException("Cannot convert " + type.getName() + " using boolean convertor.");
    }
}
