package tech.ikora.builder;

import org.apache.commons.math3.util.Pair;
import tech.ikora.error.ErrorManager;
import tech.ikora.model.*;

import java.util.ArrayList;
import java.util.List;
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

    public static void parseValues(final Variable variable, Tokens values, LineReader reader, ErrorManager errors){
        if(values.isEmpty()){
            errors.registerSyntaxError(
                    reader.getFile(),
                    String.format("Empty variable definition: %s", variable.getName()),
                    Range.fromTokens(variable.getTokens(), reader.getCurrent())
            );

            return;
        }

        if(variable instanceof ScalarVariable){
            if(values.size() > 1){
                errors.registerSyntaxError(
                        reader.getFile(),
                        String.format("Too many arguments for scalar variable, the remaining will be ignored: %s", values.withoutFirst().toString()),
                        Range.fromTokens(values.withoutFirst(), reader.getCurrent())
                );
            }

            ((ScalarVariable)variable).setValue(parseValue(values.get(0)));
        }
        else if(variable instanceof ListVariable){
            final List<Node> elements = new ArrayList<>(values.size());

            for(Token value: values){
                elements.add(parseValue(value));
            }

            ((ListVariable)variable).setValues(elements);
        }
        else if(variable instanceof DictionaryVariable){
            final List<DictionaryEntry> entries = new ArrayList<>(values.size());

            for(Token value: values){
                entries.add(parseEntry(value));
            }

            ((DictionaryVariable)variable).setEntries(entries);
        }
    }

    public static Token trimEquals(Token token) {
        return  token.trim(equalsFinder);
    }

    public static Node parseValue(Token token){
        return VariableParser.parse(token).map(variable -> (Node)variable).orElse(new Literal(token));
    }

    public static DictionaryEntry parseEntry(Token token){
        final Pair<Token, Token> keyValuePair = LexerUtils.getKeyValuePair(token);
        final Node key = parseValue(keyValuePair.getKey());
        final Node value = parseValue(keyValuePair.getValue());

        //TODO: manage the case when there is an null key or value;

        return new DictionaryEntry(key, value);
    }
}
