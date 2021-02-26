package lu.uni.serval.ikora.core.builder;

import lu.uni.serval.ikora.core.model.*;

import java.util.Optional;

public class VariableParser {
    private VariableParser(){}

    public static Optional<Variable> parse(final Token name){
        if(!ValueResolver.isVariable(name)){
            return Optional.empty();
        }

        Variable variable;

        switch (name.getText().substring(0, 1)) {
            case "$":  variable = new ScalarVariable(name); break;
            case "@":  variable = new ListVariable(name); break;
            case "&": variable = new DictionaryVariable(name); break;
            default: variable = null;
        }

        if(variable != null){
            variable.addToken(name);
        }

        return Optional.ofNullable(variable);
    }
}
