package tech.ikora.builder;

import org.apache.commons.math3.util.Pair;
import tech.ikora.error.ErrorManager;
import tech.ikora.error.ErrorMessages;
import tech.ikora.exception.InvalidArgumentException;
import tech.ikora.model.*;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Pattern;

public class VariableAssignmentParser {
    public static final Pattern equalsFinder = Pattern.compile("(\\s*=\\s*)$");

    private VariableAssignmentParser(){ }

    public static Optional<VariableAssignment> parse(LineReader reader, ErrorManager errors) throws IOException {
        Tokens tokens = LexerUtils.tokenize(reader);

        if(tokens.isEmpty()){
            errors.registerInternalError(
                    reader.getFile(),
                    ErrorMessages.EMPTY_TOKEN_NOT_EXPECTED,
                    Range.fromLine(reader.getCurrent())
            );

            return Optional.empty();
        }

        Optional<Variable> optional = VariableAssignmentParser.parseName(tokens.first());

        if(!optional.isPresent()){
            errors.registerSyntaxError(
                    reader.getFile(),
                    String.format("Invalid variable: %s", tokens.first().getText()),
                    Range.fromToken(tokens.first(), reader.getCurrent())
            );

            return Optional.empty();
        }

        VariableAssignment variable = new VariableAssignment(optional.get());
        VariableAssignmentParser.parseValues(variable, tokens.withoutFirst(), reader, errors);

        return Optional.of(variable);
    }

    public static Optional<Variable> parseName(final Token name) {
        Token cleanName = trimEquals(name);
        return VariableParser.parse(cleanName);
    }

    public static void parseValues(final VariableAssignment variable, Tokens values, LineReader reader, ErrorManager errors){
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
        final Optional<Variable> variable = VariableParser.parse(token);
        if(variable.isPresent()){
            return variable.get();
        }

        final Optional<DictionaryEntry> entry = parseEntry(token);
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
