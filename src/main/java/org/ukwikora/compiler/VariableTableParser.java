package org.ukwikora.compiler;

import org.ukwikora.model.StatementTable;
import org.ukwikora.model.LineRange;
import org.ukwikora.model.Variable;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Optional;

class VariableTableParser {
    private VariableTableParser() {}

    static public StatementTable<Variable> parse(LineReader reader) throws IOException {
        StatementTable<Variable> variableTable = new StatementTable<>();

        reader.readLine();

        while(reader.getCurrent().isValid()){
            if(LexerUtils.isBlock(reader.getCurrent().getText())){
                break;
            }

            if(reader.getCurrent().ignore()){
                reader.readLine();
                continue;
            }

            String[] tokens = LexerUtils.tokenize(reader.getCurrent().getText());

            Optional<Variable> optional = VariableParser.parse(tokens[0]);

            if(!optional.isPresent()){
                throw new InvalidParameterException(tokens[0]);
            }

            Variable variable = optional.get();

            int startLine = reader.getCurrent().getNumber();

            for (int i = 1; i < tokens.length; ++i) {
                variable.addElement(tokens[i]);
            }

            reader.readLine();

            int endLine = reader.getCurrent().getNumber();
            variable.setLineRange(new LineRange(startLine, endLine));

            variableTable.add(variable);
        }

        return variableTable;
    }
}
