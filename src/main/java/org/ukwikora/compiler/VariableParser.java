package org.ukwikora.compiler;

import org.ukwikora.model.DictionaryVariable;
import org.ukwikora.model.ListVariable;
import org.ukwikora.model.ScalarVariable;
import org.ukwikora.model.Variable;

import java.util.Optional;

public class VariableParser {
    public static Optional<Variable> parse(String name){
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
