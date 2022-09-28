package lu.uni.serval.ikora.core.runner.convertors;

import lu.uni.serval.ikora.core.runner.Resolved;
import lu.uni.serval.ikora.core.runner.exception.InternalException;
import lu.uni.serval.ikora.core.runner.exception.RunnerException;
import lu.uni.serval.ikora.core.types.*;

import java.util.HashSet;
import java.util.Set;

public class TypeConvertor {
    private static final Set<Convertor> convertors;

    static {
        convertors = new HashSet<>();
        convertors.add(new BooleanConvertor());
        convertors.add(new LogLevelTypeConvertor());
        convertors.add(new StringConvertor());
        convertors.add(new VariableConvertor());
        convertors.add(new ListConvertor());
    }

    private TypeConvertor() {}

    public static <T> T convert(BaseType baseType, Resolved value, Class<T> type) throws RunnerException {
        for(Convertor convertor: convertors){
            if(convertor.accept(baseType)){
                return convertor.convert(value, type);
            }
        }

        throw new InternalException("Invalid internal type " + baseType.getClass().getName());
    }
}
