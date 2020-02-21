package tech.ikora.builder;

import tech.ikora.error.ErrorManager;
import tech.ikora.error.ErrorMessages;
import tech.ikora.exception.InvalidDependencyException;
import tech.ikora.model.*;

import java.io.IOException;
import java.util.Optional;

class VariableTableParser {
    private VariableTableParser() {}

    public static NodeTable<Variable> parse(LineReader reader, Tokens blockTokens, ErrorManager errors) throws IOException {
        NodeTable<Variable> variableTable = new NodeTable<>();
        variableTable.setHeader(ParserUtils.parseHeaderName(reader, blockTokens, errors));

        while(reader.getCurrent().isValid() && !LexerUtils.isBlock(reader.getCurrent().getText())){
            if(reader.getCurrent().ignore()){
                reader.readLine();
                continue;
            }

            Tokens tokens = LexerUtils.tokenize(reader);

            if(tokens.isEmpty()){
                errors.registerInternalError(
                        reader.getFile(),
                        ErrorMessages.EMPTY_TOKEN_NOT_EXPECTED,
                        Range.fromLine(reader.getCurrent())
                );

                continue;
            }

            Optional<Variable> optional = VariableParser.parse(tokens.first());

            if(!optional.isPresent()){
                errors.registerInternalError(
                        reader.getFile(),
                        String.format("Invalid variable: %s", tokens.first().getText()),
                        Range.fromToken(tokens.first(), reader.getCurrent())
                );

                continue;
            }

            Variable variable = optional.get();

            for (Token token: tokens.withoutFirst()) {
                variable.addArgument(new Argument(token));
            }

            variableTable.add(variable);
        }

        return variableTable;
    }
}
