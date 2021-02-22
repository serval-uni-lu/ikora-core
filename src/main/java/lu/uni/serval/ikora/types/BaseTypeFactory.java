package lu.uni.serval.ikora.types;

import lu.uni.serval.ikora.model.DictionaryVariable;
import lu.uni.serval.ikora.model.ListVariable;
import lu.uni.serval.ikora.model.Variable;

public class BaseTypeFactory {
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
