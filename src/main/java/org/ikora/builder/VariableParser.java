package org.ikora.builder;

import org.ikora.model.*;
import org.ikora.utils.StringUtils;

import java.util.Optional;

public class VariableParser {
    public static Optional<Variable> parse(final String name){
        final String cleanName = StringUtils.trimLeft(name," =");

        if(!Value.isVariable(cleanName)){
            return Optional.empty();
        }

        Variable variable;

        switch (cleanName.substring(0, 1)) {
            case "$":  variable = new ScalarVariable(cleanName); break;
            case "@":  variable = new ListVariable(cleanName); break;
            case "&": variable = new DictionaryVariable(cleanName); break;
            default: variable = null;
        }

        return Optional.ofNullable(variable);
    }
}
