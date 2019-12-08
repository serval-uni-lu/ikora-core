package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.model.NodeTable;
import org.ikora.model.LineRange;
import org.ikora.model.Variable;

import java.io.IOException;
import java.security.InvalidParameterException;
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

            Tokens tokens = LexerUtils.tokenize(reader.getCurrent().getText());

            Optional<Token> first = tokens.first();
            if(!first.isPresent()){
                int lineNumber = reader.getCurrent().getNumber();
                errors.registerInternalError(
                        "Empty token not expected",
                        reader.getFile(),
                        new LineRange(lineNumber, lineNumber + 1)
                );

                continue;
            }

            Optional<Variable> optional = VariableParser.parse(first.get().getValue());

            if(!optional.isPresent()){
                int lineNumber = reader.getCurrent().getNumber();
                errors.registerInternalError(
                        String.format("Invalid variable: %s", first.get().getValue()),
                        reader.getFile(),
                        new LineRange(lineNumber, lineNumber + 1)
                );

                continue;
            }

            Variable variable = optional.get();

            int startLine = reader.getCurrent().getNumber();

            for (Token token: tokens.withoutFirst()) {
                variable.addElement(token.getValue());
            }

            reader.readLine();

            int endLine = reader.getCurrent().getNumber();
            variable.setLineRange(new LineRange(startLine, endLine));

            variableTable.add(variable);
        }

        return variableTable;
    }
}
