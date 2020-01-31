package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.error.ErrorMessages;
import org.ikora.exception.MalformedVariableException;
import org.ikora.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ForLoopParser {
    private ForLoopParser() {}

    public static ForLoop parse(LineReader reader, ErrorManager errors) throws IOException {
        Tokens loop = LexerUtils.tokenize(reader.getCurrent());

        Token name = extractName(reader, loop, errors);
        Variable iterator = extractIterator(reader, loop, errors);
        Step range = extractRange(reader, loop, errors);
        reader.readLine();

        List<Step> steps = new ArrayList<>();
        while (reader.getCurrent().isValid()){
            if(reader.getCurrent().ignore()) {
                reader.readLine();
                continue;
            }

            Tokens tokens = LexerUtils.tokenize(reader.getCurrent());

            if(!loop.isParent(tokens)){
                break;
            }

            Step step = StepParser.parse(reader, tokens, false, errors);
            steps.add(step);
        }

        return new ForLoop(name, iterator, range, steps);
    }

    private static Token extractName(LineReader reader, Tokens loop, ErrorManager errors) {

        if(loop.isEmpty()){
            errors.registerInternalError(
                    reader.getFile(),
                    ErrorMessages.FAILED_TO_PARSE_FORLOOP,
                    Position.fromLine(reader.getCurrent())
            );

            return Token.empty();
        }

        return loop.withoutIndent().first();
    }

    private static Variable extractIterator(LineReader reader, Tokens loop, ErrorManager errors) {
        Variable variable = Variable.invalid();

        if(loop.isEmpty()){
            errors.registerInternalError(
                    reader.getFile(),
                    ErrorMessages.FAILED_TO_LOCATE_ITERATOR_IN_FOR_LOOP,
                    Position.fromLine(reader.getCurrent())
            );
        }
        else{
            try {
                variable = Variable.create(loop.withoutIndent().get(1));
            } catch (MalformedVariableException e) {
                errors.registerInternalError(
                        reader.getFile(),
                        ErrorMessages.FAILED_TO_CREATE_ITERATOR_IN_FOR_LOOP,
                        Position.fromToken(loop.withoutIndent().get(1))
                );
            }
        }

        return variable;
    }

    private static Step extractRange(LineReader reader, Tokens loop, ErrorManager errors) {
        Tokens rangeTokens = loop.withoutIndent().withoutFirst(2);

        Step step = new InvalidStep(rangeTokens.first());

        if(rangeTokens.isEmpty()){
            errors.registerInternalError(
                    reader.getFile(),
                    ErrorMessages.EMPTY_TOKEN_SHOULD_BE_KEYWORD,
                    Position.fromTokens(loop.withoutIndent())
            );
        }
        else{
            Tokens cleanTokens = cleanInKeyword(rangeTokens);
            step = KeywordCallParser.parseLocal(reader, cleanTokens, false, errors);
        }

        return step;
    }

    private static Tokens cleanInKeyword(Tokens tokens){
        boolean first = true;

        Tokens cleanTokens = new Tokens();
        for(Token token: tokens){
            if(first){
                first = false;
                String value = token.getText();

                if(LexerUtils.compareNoCase(value, "^IN(\\s?)(.+)")){
                    String cleanValue = value.replaceAll("^([Ii])([Nn])", "").trim();
                    cleanTokens.add(new Token(cleanValue, token.getLine(), token.getStartOffset(), token.getEndOffset(), Token.Type.TEXT));
                }
            }
            else{
                cleanTokens.add(token);
            }
        }

        return cleanTokens;
    }
}
