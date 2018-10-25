package org.ukwikora.model;

import java.util.Optional;

public class VariableFactory {
    public static Optional<Variable> create(String name){
        Variable variable = null;

        String first = name.trim().substring(0, 1);
        if(first.equals("$")){
            variable = new ScalarVariable();
        }
        else if(first.equals("@")){
            variable = new ListVariable();
        }
        else if(first.equals("&")){
            variable = new DictionaryVariable();
        }

        if(variable != null){
            variable.setName(name);
        }

        return Optional.ofNullable(variable);
    }
}
