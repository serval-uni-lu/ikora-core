package tech.ikora.types;

import tech.ikora.model.DictionaryVariable;
import tech.ikora.model.ListVariable;
import tech.ikora.model.Variable;

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
