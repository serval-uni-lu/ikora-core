package org.ukwikora.builder;

import org.ukwikora.model.*;

import java.util.Optional;

public class VariableParser {
    public static Optional<Variable> parse(String name){
        if(!Value.isVariable(name)){
            return Optional.empty();
        }

        Variable variable;

        switch (name.substring(0, 1)) {
            case "$":  variable = new ScalarVariable(name); break;
            case "@":  variable = new ListVariable(name); break;
            case "&": variable = new DictionaryVariable(name); break;
            default: variable = null;
        }

        return Optional.ofNullable(variable);
    }
}
