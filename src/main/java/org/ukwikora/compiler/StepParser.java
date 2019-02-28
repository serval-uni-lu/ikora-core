package org.ukwikora.compiler;

import org.ukwikora.model.*;

import java.io.IOException;
import java.util.Arrays;

class StepParser {
    public static Step parse(LineReader reader) throws IOException {
        Step step;
        int startLine = reader.getCurrent().getNumber();

        String[] tokens = LexerUtils.tokenize(reader.getCurrent().getText());
        tokens = LexerUtils.removeIndent(tokens);

        if(isForLoop(tokens)) {
            step = parseForLoop(reader);
        }
        else if (isAssignment(tokens)){
            step = parseAssignment(reader);
        }
        else {
            step = parseKeywordCall(reader);
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

    private static Step parseAssignment(LineReader reader) throws IOException {
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
                assignment.setExpression(call);
                break;
            }
        }

        reader.readLine();

        return assignment;
    }

    private static Step parseKeywordCall(LineReader reader) throws IOException {

        String[] tokens = LexerUtils.tokenize(reader.getCurrent().getText());
        tokens = LexerUtils.removeIndent(tokens);

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
