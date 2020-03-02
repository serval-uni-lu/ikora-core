package tech.ikora.builder;

import tech.ikora.error.ErrorManager;
import tech.ikora.error.ErrorMessages;
import tech.ikora.model.*;

import java.util.Optional;

public class KeywordCallParser {
    private KeywordCallParser() {}

    public static KeywordCall parse(LineReader reader, Tokens tokens, boolean allowGherkin, ErrorManager errors) {
        Tokens callTokens = tokens.withoutIndent();

        if(callTokens.isEmpty()){
            errors.registerInternalError(
                    reader.getFile(),
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

            for(Token token: callTokens.withoutFirst()) {
                Optional<Variable> optionalVariable = VariableParser.parse(token);
                Argument argument = new Argument(optionalVariable.isPresent() ? optionalVariable.get() : new Literal(token));
                call.addArgument(argument);
            }

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
