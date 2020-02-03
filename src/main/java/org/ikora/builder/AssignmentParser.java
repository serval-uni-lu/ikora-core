package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.error.ErrorMessages;
import org.ikora.exception.InvalidDependencyException;
import org.ikora.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AssignmentParser {
    private AssignmentParser(){}

    public static Assignment parse(LineReader reader, Tokens tokens, ErrorManager errors) throws IOException {
        List<Variable> returnValues = new ArrayList<>();
        KeywordCall expression = null;

        int offset = 0;
        boolean leftSide = true;
        Tokens assignmentTokens = tokens.withoutIndent();
        for(Token token: assignmentTokens){
            Token clean = VariableParser.trimEquals(token);

            if(!clean.isEmpty()){
                if(leftSide && ValueLinker.isVariable(clean)){
                    Optional<Variable> variable = VariableParser.parse(clean);

                    if(variable.isPresent()){
                        Variable returnValue = variable.get();
                        returnValues.add(returnValue);
                    }
                }
                else if(!leftSide){
                    expression = KeywordCallParser.parseLocal(reader, assignmentTokens.withoutFirst(offset), false, errors);
                    break;
                }
            }

            leftSide &= !token.getText().contains("=");
            ++offset;
        }

        Assignment assignment = null;

        try{
            Token name = expression != null ? expression.getName() : Token.empty();
            assignment = new Assignment(name, returnValues, expression);

            if(!assignment.getKeywordCall().isPresent()){
                errors.registerSyntaxError(
                        reader.getFile(),
                        ErrorMessages.ASSIGNMENT_SHOULD_HAVE_LEFT_HAND_OPERAND,
                        Position.fromTokens(tokens, reader.getCurrent())
                );
            }
        }
        catch (InvalidDependencyException e) {
            errors.registerInternalError(
                    reader.getFile(),
                    ErrorMessages.FAILED_TO_CREATE_DEPENDENCY,
                    Position.fromTokens(tokens, reader.getCurrent())
            );
        }

        reader.readLine();

        return assignment;
    }
}
