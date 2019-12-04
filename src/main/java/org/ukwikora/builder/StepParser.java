package org.ukwikora.builder;

import org.ukwikora.error.ErrorManager;
import org.ukwikora.exception.InvalidDependencyException;
import org.ukwikora.model.*;

import java.io.IOException;
import java.util.Arrays;

class StepParser {
    public static Step parse(LineReader reader, String[] tokens, ErrorManager errors) throws IOException {
        return parse(reader, tokens, "", errors);
    }

    public static Step parse(LineReader reader, String[] tokens, String ignoreTag, ErrorManager errors) throws IOException {
        Step step;
        int startLine = reader.getCurrent().getNumber();

        tokens = LexerUtils.removeIndent(tokens);
        tokens = LexerUtils.removeTag(tokens, ignoreTag);

        if(isForLoop(tokens)) {
            step = parseForLoop(reader);
        }
        else if (isAssignment(tokens)){
            step = parseAssignment(reader, errors);
        }
        else {
            step = parseKeywordCall(reader, tokens);
        }

        int endLine = reader.getCurrent().getNumber();
        step.setLineRange(new LineRange(startLine, endLine));

        return step;
    }

    private static Step parseForLoop(LineReader reader) throws IOException {
        ForLoop forLoop = new ForLoop();
        Line loop = reader.getCurrent();
        forLoop.setName(loop.getText());

        while (reader.readLine().isValid()){
            if(!LexerUtils.isInBlock(loop.getText(), reader.getCurrent().getText())){
                break;
            }
        }

        return forLoop;
    }

    private static Step parseAssignment(LineReader reader, ErrorManager errors) throws IOException {
        Assignment assignment = new Assignment();
        assignment.setName(reader.getCurrent().getText());

        String[] tokens = LexerUtils.tokenize(reader.getCurrent().getText());

        for(int i = 0; i < tokens.length; ++i){
            String token = tokens[i].replaceAll("(\\s*)=(\\s*)$", "");
            token = token.replaceAll("^(\\s*)=(\\s*)", "");

            if(token.isEmpty()){
                continue;
            }

            if(Value.isVariable(token)){
                VariableParser.parse(token).ifPresent(variable -> {
                    variable.setAssignment(assignment);
                    assignment.addReturnValue(variable);
                });
            }
            else{
                KeywordCall call = getKeywordCall(Arrays.copyOfRange(tokens, i, tokens.length));

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
        }

        reader.readLine();

        return assignment;
    }

    private static Step parseKeywordCall(LineReader reader, String[] tokens) throws IOException {
        KeywordCall call = getKeywordCall(tokens);
        reader.readLine();

        return call;
    }

    private static KeywordCall getKeywordCall(String[] tokens) {
        KeywordCall call = new KeywordCall();

        if(tokens.length > 0) {
            call.setName(tokens[0]);
        }

        if (tokens.length > 1) {
            for(int i = 1; i < tokens.length; ++i) {
                call.addParameter(tokens[i]);
            }
        }
        return call;
    }

    private static boolean isAssignment(String[] tokens){
        return LexerUtils.compareNoCase(tokens[0], "^((\\$|@|&)\\{)(.*)(\\})(\\s?)(=?)");
    }

    private static boolean isForLoop(String[] tokens) {
        return tokens[0].equalsIgnoreCase(":FOR");
    }
}
