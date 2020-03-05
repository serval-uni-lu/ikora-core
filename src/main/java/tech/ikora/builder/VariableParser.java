package tech.ikora.builder;

import org.apache.commons.math3.util.Pair;
import tech.ikora.error.ErrorManager;
import tech.ikora.exception.InvalidArgumentException;
import tech.ikora.model.*;

import java.util.Optional;
import java.util.regex.Pattern;

public class VariableParser {
    private VariableParser(){}

    public static final Pattern equalsFinder = Pattern.compile("(\\s*=\\s*)$");

    public static Optional<Variable> parseName(final Token name){
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

    public static void parseValues(final Variable variable, Tokens values, LineReader reader, ErrorManager errors){
        if(values.isEmpty()){
            errors.registerSyntaxError(
                    reader.getFile(),
                    String.format("Empty variable definition: %s", variable.getName()),
                    Range.fromTokens(variable.getTokens(), reader.getCurrent())
            );
        }

        for(Token value: values){
            try {
                variable.addValue(parseValue(value));
            } catch (InvalidArgumentException e) {
                errors.registerSyntaxError(
                        reader.getFile(),
                        String.format("Invalid value for a variable: %s", e.getMessage()),
                        Range.fromTokens(values.withoutFirst(), reader.getCurrent())
                );
            }
        }
    }

    public static Token trimEquals(Token token) {
        return  token.trim(equalsFinder);
    }

    public static Node parseValue(Token token){
        final Optional<Variable> variable = VariableParser.parseName(token);
        if(variable.isPresent()){
            return variable.get();
        }

        final Optional<DictionaryEntry> entry = VariableParser.parseEntry(token);
        if(entry.isPresent()){
            return entry.get();
        }

        return new Literal(token);
    }

    public static Optional<DictionaryEntry> parseEntry(Token token){
        final Pair<Token, Token> keyValuePair = LexerUtils.getKeyValuePair(token);

        if(keyValuePair.getKey().isEmpty() || keyValuePair.getValue().isEmpty()){
            return Optional.empty();
        }

        final Node key = parseValue(keyValuePair.getKey());
        final Node value = parseValue(keyValuePair.getValue());

        return Optional.of(new DictionaryEntry(key, value));
    }
}
