package lu.uni.serval.ikora.core.builder;

import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.error.ErrorMessages;
import lu.uni.serval.ikora.core.model.*;

import java.util.Optional;

public class KeywordCallParser {
    private KeywordCallParser() {}

    public static KeywordCall parse(LineReader reader, Tokens tokens, boolean allowGherkin, ErrorManager errors) {
        Tokens callTokens = tokens.withoutIndent();

        if(callTokens.isEmpty()){
            errors.registerInternalError(
                    reader.getSource(),
                    ErrorMessages.EMPTY_TOKEN_SHOULD_BE_KEYWORD,
                    Range.fromLine(reader.getCurrent())
            );
        }
        else{
            Token rawName = callTokens.first();
            Token name = getKeywordCallName(rawName, allowGherkin);
            Gherkin gherkin = new Gherkin(rawName);

            KeywordCall call = new KeywordCall(name);
            call.setGherkin(gherkin);

            final NodeList<Argument> arguments = new NodeList<>();

            for(Token token: callTokens.withoutFirst()) {
                Optional<Variable> variable = VariableParser.parse(token);
                Argument argument = new Argument(variable.isPresent() ? variable.get() : new Literal(token));
                arguments.add(argument);
            }

            call.setArgumentList(arguments);

            return call;
        }

        return null;
    }

    private static Token getKeywordCallName(Token raw, boolean allowGherkin) {
        if(!allowGherkin){
            return raw;
        }

        return Gherkin.getCleanName(raw);
    }
}
