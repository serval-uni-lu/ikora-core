package lu.uni.serval.ikora.core.types;

import lu.uni.serval.ikora.core.model.DictionaryVariable;
import lu.uni.serval.ikora.core.model.ListVariable;
import lu.uni.serval.ikora.core.model.Variable;

public class BaseTypeFactory {
    private BaseTypeFactory() {}

    public static BaseType fromVariable(Variable parameter) {
        if(parameter instanceof DictionaryVariable){
            return new DictionaryType(parameter.getName());
        }
        else if(parameter instanceof ListVariable){
            return new ListType(parameter.getName());
        }

        return new StringType(parameter.getName());
    }
}
