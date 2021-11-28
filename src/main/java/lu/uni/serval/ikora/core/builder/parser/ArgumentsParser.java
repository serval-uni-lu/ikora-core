package lu.uni.serval.ikora.core.builder.parser;

import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.error.ErrorMessages;
import lu.uni.serval.ikora.core.exception.MalformedVariableException;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.types.StringType;
import lu.uni.serval.ikora.core.utils.StringUtils;

import java.util.Iterator;

public class ArgumentsParser {
    private ArgumentsParser() {}

    public static boolean is(Token label){
        return StringUtils.matchesIgnoreCase(label, "\\[arguments\\]");
    }

    public static NodeList<Argument> parse(LineReader reader, Token label, Iterator<Token> tokenIterator, ErrorManager errors) {
        final NodeList<Argument> arguments = new NodeList<>(label);

        int position = 0;
        while (tokenIterator.hasNext()){
            Token token = tokenIterator.next();

            try {
                final Argument argument = new Argument(
                        Variable.create(token),
                        new StringType(token.getText()),
                        position++
                );

                arguments.add(argument);

            } catch (MalformedVariableException e) {
                errors.registerSyntaxError(
                        reader.getSource(),
                        String.format("%s: %s", ErrorMessages.FAILED_TO_PARSE_PARAMETER, e.getMessage()),
                        Range.fromToken(token)
                );
            }
        }

        return arguments;
    }
}
