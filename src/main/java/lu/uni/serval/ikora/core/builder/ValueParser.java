package lu.uni.serval.ikora.core.builder;

import lu.uni.serval.ikora.core.model.*;
import org.apache.commons.math3.util.Pair;

import java.util.Optional;

public class ValueParser {
    private ValueParser() {}

    public static Optional<DictionaryEntry> parseEntry(Token token){
        final Pair<Token, Token> keyValuePair = LexerUtils.getKeyValuePair(token);

        if(keyValuePair.getKey().isEmpty() || keyValuePair.getValue().isEmpty()){
            return Optional.empty();
        }

        final SourceNode key = parseValue(keyValuePair.getKey());
        final SourceNode value = parseValue(keyValuePair.getValue());

        return Optional.of(new DictionaryEntry(key, value));
    }

    public static Value parseValue(Token token){
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
}
