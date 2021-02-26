package lu.uni.serval.ikora.core.builder;

import lu.uni.serval.ikora.core.model.Literal;
import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.model.Variable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LiteralParser {
    private LiteralParser() {}

    public static Literal parse(Token token){
        final List<Token> variableNames = ValueResolver.findVariables(token);

        final List<Variable> variables = variableNames.stream().map(VariableParser::parse)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return new Literal(token, variables);
    }
}
