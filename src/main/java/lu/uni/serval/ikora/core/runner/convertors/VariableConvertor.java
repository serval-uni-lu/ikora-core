package lu.uni.serval.ikora.core.runner.convertors;

import lu.uni.serval.ikora.core.runner.Resolved;
import lu.uni.serval.ikora.core.runner.exception.InternalException;
import lu.uni.serval.ikora.core.types.VariableType;

public class VariableConvertor extends Convertor {
    VariableConvertor() {
        super(VariableType.class);
    }

    public <T> T convert(Resolved value, Class<T> type) throws InternalException {
        if(type.equals(String.class)){
            return (T)value.getOrigin().getName();
        }

        throw new InternalException("Cannot convert " + type.getName() + " using boolean convertor.");
    }
}
