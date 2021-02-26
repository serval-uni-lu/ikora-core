package lu.uni.serval.ikora.core.builder;

import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.error.ErrorMessages;
import lu.uni.serval.ikora.core.model.*;

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
                    reader.getSource(),
                    ErrorMessages.EMPTY_TOKEN_NOT_EXPECTED,
                    Range.fromLine(reader.getCurrent())
            );

            return Optional.empty();
        }

        Optional<Variable> optional = VariableAssignmentParser.parseName(tokens.first());

        if(!optional.isPresent()){
            errors.registerSyntaxError(
                    reader.getSource(),
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
                    reader.getSource(),
                    String.format("Empty variable definition: %s", variable.getNameToken()),
                    Range.fromTokens(variable.getTokens(), reader.getCurrent())
            );
        }

        for(Token value: values){
            variable.addValue(ValueParser.parseValue(value));
        }
    }

    public static Token trimEquals(Token token) {
        return  token.trim(equalsFinder);
    }
}
