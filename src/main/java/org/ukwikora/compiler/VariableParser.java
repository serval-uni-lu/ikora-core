package org.ukwikora.compiler;

import org.ukwikora.model.DictionaryVariable;
import org.ukwikora.model.ListVariable;
import org.ukwikora.model.ScalarVariable;
import org.ukwikora.model.Variable;

import java.util.Optional;

class VariableParser {
    public static Optional<Variable> parse(String name){
        Variable variable;
        switch (name.trim().substring(0, 1)) {
            case "$":  variable = new ScalarVariable(name); break;
            case "@":  variable = new ListVariable(name); break;
            case "&": variable = new DictionaryVariable(name); break;
            default: variable = null;
        }

        return Optional.ofNullable(variable);
    }
}
