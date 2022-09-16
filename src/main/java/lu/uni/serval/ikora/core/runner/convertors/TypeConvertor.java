package lu.uni.serval.ikora.core.runner.convertors;

import lu.uni.serval.ikora.core.runner.exception.InternalException;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;
import lu.uni.serval.ikora.core.types.BaseType;
import lu.uni.serval.ikora.core.types.BooleanType;
import lu.uni.serval.ikora.core.types.LogLevelType;
import lu.uni.serval.ikora.core.types.StringType;

public class TypeConvertor {
    private TypeConvertor() {}

    public static <T> T convert(BaseType baseType, String value, Class<T> type) throws RunnerException {
        if (BooleanType.class.equals(baseType.getClass())) {
            return BooleanConvertor.convert(value, type);
        }

        if(LogLevelType.class.equals(baseType.getClass())) {
            return LogLevelTypeConvertor.convert(value, type);
        }

        if(StringType.class.equals(baseType.getClass())) {
            return StringConvertor.convert(value, type);
        }

        throw new InternalException("Invalid internal type " + baseType.getClass().getName());
    }
}
