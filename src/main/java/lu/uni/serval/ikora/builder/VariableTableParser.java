package lu.uni.serval.ikora.builder;

import lu.uni.serval.ikora.error.ErrorManager;
import lu.uni.serval.ikora.model.SourceNodeTable;
import lu.uni.serval.ikora.model.Tokens;
import lu.uni.serval.ikora.model.VariableAssignment;

import java.io.IOException;
import java.util.Optional;

class VariableTableParser {
    private VariableTableParser() {}

    public static SourceNodeTable<VariableAssignment> parse(LineReader reader, Tokens blockTokens, ErrorManager errors) throws IOException {
        SourceNodeTable<VariableAssignment> variableTable = new SourceNodeTable<>();
        variableTable.setHeader(ParserUtils.parseHeaderName(reader, blockTokens, errors));

        while(reader.getCurrent().isValid() && !LexerUtils.isBlock(reader.getCurrent().getText())){
            if(reader.getCurrent().ignore()){
                reader.readLine();
                continue;
            }

            Optional<VariableAssignment> variable = VariableAssignmentParser.parse(reader, errors);
            variable.ifPresent(variableTable::add);
        }

        return variableTable;
    }
}
