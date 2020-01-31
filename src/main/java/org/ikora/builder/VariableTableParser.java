package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.error.ErrorMessages;
import org.ikora.exception.InvalidDependencyException;
import org.ikora.model.*;

import java.io.IOException;
import java.util.Optional;

class VariableTableParser {
    private VariableTableParser() {}

    public static NodeTable<Variable> parse(LineReader reader, ErrorManager errors) throws IOException {
        NodeTable<Variable> variableTable = new NodeTable<>();

        reader.readLine();

        while(reader.getCurrent().isValid()){
            if(LexerUtils.isBlock(reader.getCurrent().getText())){
                break;
            }

            if(reader.getCurrent().ignore()){
                reader.readLine();
                continue;
            }

            Tokens tokens = LexerUtils.tokenize(reader.getCurrent());

            if(tokens.isEmpty()){
                errors.registerInternalError(
                        reader.getFile(),
                        ErrorMessages.EMPTY_TOKEN_NOT_EXPECTED,
                        Position.fromLine(reader.getCurrent())
                );

                continue;
            }

            Optional<Variable> optional = VariableParser.parse(tokens.first());

            if(!optional.isPresent()){
                errors.registerInternalError(
                        reader.getFile(),
                        String.format("Invalid variable: %s", tokens.first().getText()),
                        Position.fromToken(tokens.first())
                );

                continue;
            }

            Variable variable = optional.get();

            for (Token token: tokens.withoutFirst()) {
                try {
                    variable.addArgument(new Argument(variable, token));
                } catch (InvalidDependencyException e) {
                    errors.registerInternalError(
                            reader.getFile(),
                            String.format("Invalid variable dependency: %s", token),
                            Position.fromToken(token)
                    );
                }
            }

            reader.readLine();

            variableTable.add(variable);
        }

        return variableTable;
    }
}
