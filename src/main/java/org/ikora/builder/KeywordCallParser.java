package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.error.ErrorMessages;
import org.ikora.exception.InvalidDependencyException;
import org.ikora.model.Argument;
import org.ikora.model.Gherkin;
import org.ikora.model.KeywordCall;

import java.io.IOException;
import java.util.Optional;

public class KeywordCallParser {
    private KeywordCallParser() {}

    public static KeywordCall parse(LineReader reader, Tokens tokens, boolean allowGherkin, ErrorManager errors) throws IOException {
        KeywordCall call = parseLocal(reader, tokens, allowGherkin, errors);
        reader.readLine();
        return call;
    }

    public static KeywordCall parseLocal(LineReader reader, Tokens tokens, boolean allowGherkin, ErrorManager errors) {
        Optional<Token> first =  tokens.first();

        if(!first.isPresent()){
            errors.registerInternalError(
                    reader.getFile(),
                    ErrorMessages.EMPTY_TOKEN_SHOULD_BE_KEYWORD,
                    ParserUtils.getPosition(reader.getCurrent())
            );
        }
        else{
            String rawName = first.get().getValue();
            String name = getKeywordCallName(rawName, allowGherkin);
            Gherkin gherkin = new Gherkin(rawName);

            KeywordCall call = new KeywordCall(name);
            call.setGherkin(gherkin);

            for(Token token: tokens.withoutFirst()) {
                try {
                    Argument argument = new Argument(call, token.getValue());
                    argument.setPosition(ParserUtils.getPosition(token, token));

                    call.addArgument(argument);
                } catch (InvalidDependencyException e) {
                    errors.registerInternalError(
                            reader.getFile(),
                            "Failed to register parameter to keyword call",
                            ParserUtils. getPosition(token, token)
                    );
                }
            }

            call.setPosition(ParserUtils.getPosition(tokens.withoutIndent()));
            return call;
        }

        return null;
    }

    private static String getKeywordCallName(String raw, boolean allowGherkin) {
        if(!allowGherkin){
            return raw;
        }

        return Gherkin.getCleanName(raw);
    }
}
