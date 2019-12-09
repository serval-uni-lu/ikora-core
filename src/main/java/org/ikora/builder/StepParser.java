package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.exception.InvalidDependencyException;
import org.ikora.model.*;

import java.io.IOException;
import java.util.Optional;

class StepParser {
    public static Step parse(LineReader reader, Tokens tokens, ErrorManager errors) throws IOException {
        return parse(reader, tokens, "", errors);
    }

    public static Step parse(LineReader reader, Tokens tokens, String ignoreTag, ErrorManager errors) throws IOException {
        Step step;
        int startLine = reader.getCurrent().getNumber();

        tokens = tokens.withoutIndent();
        tokens = tokens.withoutTag(ignoreTag);

        if(isForLoop(reader, tokens, errors)) {
            step = parseForLoop(reader);
        }
        else if (isAssignment(reader, tokens, errors)){
            step = parseAssignment(reader, errors);
        }
        else {
            step = parseKeywordCall(reader, tokens, errors);
        }

        int endLine = reader.getCurrent().getNumber();
        step.setLineRange(new LineRange(startLine, endLine));

        return step;
    }

    private static Step parseForLoop(LineReader reader) throws IOException {
        ForLoop forLoop = new ForLoop();
        Tokens loop = LexerUtils.tokenize(reader.getCurrent().getText());
        forLoop.setName("TO DO");

        while (reader.readLine().isValid()){
            Tokens tokens = LexerUtils.tokenize(reader.getCurrent().getText());

            if(!loop.isParent(tokens)){
                break;
            }
        }

        return forLoop;
    }

    private static Step parseAssignment(LineReader reader, ErrorManager errors) throws IOException {
        Assignment assignment = new Assignment();
        assignment.setName(reader.getCurrent().getText());

        Tokens tokens = LexerUtils.tokenize(reader.getCurrent().getText()).withoutIndent();

        int offset = 0;
        for(Token token: tokens){
            String value = token.getValue().replaceAll("(\\s*)=(\\s*)$", "");
            value = value.replaceAll("^(\\s*)=(\\s*)", "");

            if(value.isEmpty()){
                ++offset;
                continue;
            }

            if(Value.isVariable(value)){
                VariableParser.parse(value).ifPresent(variable -> {
                    variable.setAssignment(assignment);
                    assignment.addReturnValue(variable);
                });
            }
            else{
                KeywordCall call = getKeywordCall(reader, tokens.withoutFirst(offset), errors);

                try {
                    assignment.setExpression(call);
                } catch (InvalidDependencyException e) {
                    errors.registerInternalError(
                            "Dependency failed to be created, this should not happen",
                            call.getFile().getFile(),
                            call.getLineRange()
                    );
                }
                break;
            }

            ++offset;
        }

        reader.readLine();

        return assignment;
    }

    private static Step parseKeywordCall(LineReader reader, Tokens tokens, ErrorManager errors) throws IOException {
        KeywordCall call = getKeywordCall(reader, tokens, errors);
        reader.readLine();
        return call;
    }

    private static KeywordCall getKeywordCall(LineReader reader, Tokens tokens, ErrorManager errors) throws IOException {
        KeywordCall call = new KeywordCall();

        Optional<Token> first =  tokens.first();

        if(!first.isPresent()){
            int lineNumber = reader.getCurrent().getNumber();

            errors.registerInternalError(
                    "Empty token should be a keyword call",
                    reader.getFile(),
                    new LineRange(lineNumber, lineNumber + 1)
            );
        }
        else{
            call.setName(first.get().getValue());

            for(Token token: tokens.withoutFirst()) {
                call.addParameter(token.getValue());
            }
        }

        return call;
    }

    private static boolean isAssignment(LineReader reader, Tokens tokens, ErrorManager errors){
        Optional<Token> first = tokens.first();

        if(!first.isPresent()) {
            int lineNumber = reader.getCurrent().getNumber();

            errors.registerInternalError(
                    "Empty token should be a keyword call",
                    reader.getFile(),
                    new LineRange(lineNumber, lineNumber + 1)
            );

            return false;
        }

        return first.get().isAssignment();
    }

    private static boolean isForLoop(LineReader reader, Tokens tokens, ErrorManager errors) {
        Optional<Token> first =  tokens.first();

        if(!first.isPresent()){
            int lineNumber = reader.getCurrent().getNumber();

            errors.registerInternalError(
                    "Empty token should be a keyword call",
                    reader.getFile(),
                    new LineRange(lineNumber, lineNumber + 1)
            );

            return false;
        }

        return first.get().isForLoop();
    }
}
