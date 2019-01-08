package org.ukwikora.model;

import java.util.Optional;

public class VariableFactory {
    public static Optional<Variable> create(String name){
        Variable variable;
        switch (name.trim().substring(0, 1)) {
            case "$":  variable = new ScalarVariable(); break;
            case "@":  variable = new ListVariable(); break;
            case "&": variable = new DictionaryVariable(); break;
            default: variable = null;
        }

        if(variable != null){
            variable.setName(name);
        }

        return Optional.ofNullable(variable);
    }
}
