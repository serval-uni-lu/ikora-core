package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.error.ErrorMessages;
import org.ikora.model.NodeTable;
import org.ikora.model.Token;
import org.ikora.model.Variable;

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

            Optional<Token> first = tokens.first();
            if(!first.isPresent()){
                errors.registerInternalError(
                        reader.getFile(),
                        ErrorMessages.EMPTY_TOKEN_NOT_EXPECTED,
                        ParserUtils.getPosition(reader.getCurrent())
                );

                continue;
            }

            Optional<Variable> optional = VariableParser.parse(first.get());

            if(!optional.isPresent()){
                errors.registerInternalError(
                        reader.getFile(),
                        String.format("Invalid variable: %s", first.get().getValue()),
                        ParserUtils.getPosition(reader.getCurrent())
                );

                continue;
            }

            Variable variable = optional.get();

            for (Token token: tokens.withoutFirst()) {
                variable.addElement(token);
            }

            reader.readLine();

            variable.setPosition(ParserUtils.getPosition(tokens));
            variableTable.add(variable);
        }

        return variableTable;
    }
}
