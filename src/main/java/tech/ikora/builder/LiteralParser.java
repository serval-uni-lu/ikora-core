package tech.ikora.builder;

import tech.ikora.model.Literal;
import tech.ikora.model.Token;
import tech.ikora.model.Variable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LiteralParser {
    public static Literal parse(Token token){
        final List<Token> variableNames = ValueResolver.findVariables(token);

        final List<Variable> variables = variableNames.stream().map(VariableParser::parse)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return new Literal(token, variables);
    }
}
