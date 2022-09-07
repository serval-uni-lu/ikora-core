package lu.uni.serval.ikora.core.runner.convertors;

import lu.uni.serval.ikora.core.model.SourceNode;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;
import lu.uni.serval.ikora.core.types.BaseType;
import lu.uni.serval.ikora.core.types.BooleanType;

public class TypeConvertor {
    private TypeConvertor() {}

    public static <T> T convert(BaseType baseType, String value, Runtime runtime, Class<T> type) throws RunnerException {
        if (BooleanType.class.equals(baseType.getClass())) {
            return BooleanConvertor.convert(value, type);
        }

        return null;
    }
}
