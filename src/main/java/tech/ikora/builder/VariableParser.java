package tech.ikora.builder;

import tech.ikora.model.*;

import java.util.Optional;
import java.util.regex.Pattern;

public class VariableParser {
    private VariableParser(){}

    public static final Pattern equalsFinder = Pattern.compile("(\\s*=\\s*)$");

    public static Optional<Variable> parse(final Token name){
        Token cleanName = trimEquals(name);

        if(!ValueLinker.isVariable(cleanName)){
            return Optional.empty();
        }

        Variable variable;

        switch (cleanName.getText().substring(0, 1)) {
            case "$":  variable = new ScalarVariable(cleanName); break;
            case "@":  variable = new ListVariable(cleanName); break;
            case "&": variable = new DictionaryVariable(cleanName); break;
            default: variable = null;
        }

        if(variable != null){
            variable.addToken(name);
        }

        return Optional.ofNullable(variable);
    }

    public static Token trimEquals(Token token) {
        return  token.trim(equalsFinder);
    }
}