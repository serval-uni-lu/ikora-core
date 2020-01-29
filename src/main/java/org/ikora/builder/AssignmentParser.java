package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.error.ErrorMessages;
import org.ikora.exception.InvalidDependencyException;
import org.ikora.model.*;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Pattern;

public class AssignmentParser {
    private AssignmentParser(){}

    public static Assignment parse(LineReader reader, ErrorManager errors) throws IOException {
        Assignment assignment = new Assignment();
        Tokens tokens = LexerUtils.tokenize(reader.getCurrent()).withoutIndent();

        int offset = 0;
        boolean leftSide = true;
        for(Token token: tokens){
            try {
                Token clean = VariableParser.trimEquals(token);

                if(!clean.isEmpty()){
                    if(leftSide && Value.isVariable(clean)){
                        Optional<Variable> variable = VariableParser.parse(clean);

                        if(variable.isPresent()){
                            variable.get().setAssignment(assignment);
                            variable.get().setPosition(ParserUtils.getPosition(token, token));
                            assignment.addReturnValue(variable.get());
                        }
                    }
                    else if(!leftSide){
                        KeywordCall call = KeywordCallParser.parseLocal(reader, tokens.withoutFirst(offset), false, errors);

                        if(call != null){
                            assignment.setExpression(call);
                        }

                        break;
                    }
                }

                leftSide &= !token.getValue().contains("=");
            } catch (InvalidDependencyException e) {
                errors.registerInternalError(
                        reader.getFile(),
                        ErrorMessages.FAILED_TO_CREATE_DEPENDENCY,
                        ParserUtils.getPosition(token, token)
                );
            }
            ++offset;
        }

        if(!assignment.getKeywordCall().isPresent()){
            errors.registerSyntaxError(
                    reader.getFile(),
                    ErrorMessages.ASSIGNMENT_SHOULD_HAVE_LEFT_HAND_OPERAND,
                    ParserUtils.getPosition(tokens)
            );
        }

        reader.readLine();

        return assignment;
    }
}
