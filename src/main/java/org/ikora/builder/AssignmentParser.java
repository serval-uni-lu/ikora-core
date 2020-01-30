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

    public static Assignment parse(LineReader reader, ErrorManager errors) throws IOException {
        Tokens tokens = LexerUtils.tokenize(reader.getCurrent()).withoutIndent();

        List<Variable> returnValues = new ArrayList<>();
        KeywordCall expression = null;

        int offset = 0;
        boolean leftSide = true;
        for(Token token: tokens){
            Token clean = VariableParser.trimEquals(token);

            if(!clean.isEmpty()){
                if(leftSide && Value.isVariable(clean)){
                    Optional<Variable> variable = VariableParser.parse(clean);

                    if(variable.isPresent()){
                        Variable returnValue = variable.get();
                        returnValue.setPosition(ParserUtils.getPosition(token, token));
                        returnValues.add(returnValue);
                    }
                }
                else if(!leftSide){
                    expression = KeywordCallParser.parseLocal(reader, tokens.withoutFirst(offset), false, errors);
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
                        ParserUtils.getPosition(tokens)
                );
            }
        }
        catch (InvalidDependencyException e) {
            errors.registerInternalError(
                    reader.getFile(),
                    ErrorMessages.FAILED_TO_CREATE_DEPENDENCY,
                    ParserUtils.getPosition(tokens)
            );
        }

        reader.readLine();

        return assignment;
    }
}
