package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.error.ErrorMessages;
import org.ikora.exception.InvalidDependencyException;
import org.ikora.model.*;

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
        if(tokens.isEmpty()){
            errors.registerInternalError(
                    reader.getFile(),
                    ErrorMessages.EMPTY_TOKEN_SHOULD_BE_KEYWORD,
                    Position.fromLine(reader.getCurrent())
            );
        }
        else{
            Token rawName = tokens.first();
            Token name = getKeywordCallName(rawName, allowGherkin);
            Gherkin gherkin = new Gherkin(rawName);

            KeywordCall call = new KeywordCall(name);
            call.setGherkin(gherkin);

            for(Token token: tokens.withoutFirst()) {
                try {
                    Argument argument = new Argument(call, token);
                    call.addArgument(argument);
                } catch (InvalidDependencyException e) {
                    errors.registerInternalError(
                            reader.getFile(),
                            "Failed to register parameter to keyword call",
                            Position.fromToken(token)
                    );
                }
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
