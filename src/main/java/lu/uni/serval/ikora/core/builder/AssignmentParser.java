package lu.uni.serval.ikora.core.builder;

import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.error.ErrorMessages;
import lu.uni.serval.ikora.core.model.*;

import java.util.ArrayList;
import java.util.List;

public class AssignmentParser {
    private AssignmentParser(){}

    public static Assignment parse(LineReader reader, Tokens tokens, ErrorManager errors) {
        final List<Variable> returnValues = new ArrayList<>();
        final Tokens assignmentTokens = tokens.withoutIndent();

        int offset = 0;

        for(Token token: tokens.withoutIndent()){
            final Token clean = VariableAssignmentParser.trimEquals(token);

            if(!ValueResolver.isVariable(clean)){
                break;
            }

            VariableParser.parse(clean).ifPresent(returnValues::add);

            ++offset;
        }

        final KeywordCall expression = offset < assignmentTokens.size()
                ? KeywordCallParser.parse(reader, assignmentTokens.withoutFirst(offset), false, errors)
                : null;

        final Token name = expression != null ? expression.getNameToken() : Token.empty();
        final Assignment assignment = new Assignment(name, returnValues, expression);

        if(!assignment.getKeywordCall().isPresent()){
            errors.registerSyntaxError(
                    reader.getSource(),
                    ErrorMessages.ASSIGNMENT_SHOULD_HAVE_LEFT_HAND_OPERAND,
                    Range.fromTokens(tokens, reader.getCurrent())
            );
        }

        return assignment;
    }
}
