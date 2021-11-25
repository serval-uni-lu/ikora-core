package lu.uni.serval.ikora.core.builder;

import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.error.ErrorMessages;
import lu.uni.serval.ikora.core.exception.MalformedVariableException;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.utils.StringUtils;

import java.util.Iterator;

public class ArgumentsParser {
    private ArgumentsParser() {}

    public static boolean is(Token label){
        return StringUtils.matchesIgnoreCase(label, "\\[arguments\\]");
    }

    public static NodeList<Variable> parse(LineReader reader, Token label, Iterator<Token> tokenIterator, ErrorManager errors) {
        final NodeList<Variable> arguments = new NodeList<>(label);

        while (tokenIterator.hasNext()){
            Token token = tokenIterator.next();

            try {
                arguments.add(Variable.create(token));
            } catch (MalformedVariableException e) {
                errors.registerSyntaxError(
                        reader.getSource(),
                        String.format("%s: %s", ErrorMessages.FAILED_TO_PARSE_PARAMETER, e.getMessage()),
                        Range.fromToken(token, reader.getCurrent())
                );
            }
        }

        return arguments;
    }
}
