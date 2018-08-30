package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.*;

import java.io.IOException;
import java.util.Arrays;

public class StepParser {
    public static Step parse(LineReader reader) throws IOException {
        Step step;

        String[] tokens = reader.getCurrent().tokenize();
        tokens = Utils.removeIndent(tokens);

        if(tokens[0].equalsIgnoreCase(":FOR")) {
            step = parseForLoop(reader);
        }
        else if (Utils.compareNoCase(tokens[0], "^(\\$\\{)(.*)(\\})(\\s?)(=?)")){
            step = parseAssignment(reader);
        }
        else {
            step = parseKeywordNameAndParameter(reader);
        }

        return step;
    }

    private static Step parseForLoop(LineReader reader) throws IOException {
        ForLoop forLoop = new ForLoop();
        Line loop = reader.getCurrent();
        forLoop.setName(loop.getText());

        while (reader.readLine().isValid()){
            if(!reader.getCurrent().isInBlock(loop)){
                break;
            }
        }

        return forLoop;
    }

    private static Step parseAssignment(LineReader reader) throws IOException {
        Assignment assignment = new Assignment();
        assignment.setName(reader.getCurrent().getText());

        String[] tokens = reader.getCurrent().tokenize();

        for(int i = 0; i < tokens.length; ++i){
            String token = tokens[i].replaceAll("(\\s*)=(\\s*)$", "");
            token = token.replaceAll("^(\\s*)=(\\s*)", "");

            if(token.isEmpty()){
                continue;
            }

            if(Argument.isVariable(token)){
                assignment.addReturnValue(token);
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

    static private Step parseKeywordNameAndParameter(LineReader reader) throws IOException {

        String[] tokens = reader.getCurrent().tokenize();
        tokens = Utils.removeIndent(tokens);

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
}
