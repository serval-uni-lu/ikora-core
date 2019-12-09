package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.model.KeywordDefinition;
import org.ikora.model.LineRange;

import java.io.IOException;
import java.util.Optional;

public class ParserUtils {
    static void parseName(LineReader reader, Tokens tokens, KeywordDefinition keyword, ErrorManager errors) throws IOException {
        if(tokens.size() > 1){
            int lineNumber = reader.getCurrent().getNumber();

            errors.registerSyntaxError(
                    "Test definition cannot take arguments",
                    reader.getFile(),
                    new LineRange(lineNumber, lineNumber + 1)
            );

            reader.readLine();
            return;
        }

        Optional<Token> first = tokens.first();

        if(!first.isPresent()){
            int lineNumber = reader.getCurrent().getNumber();

            errors.registerInternalError(
                    "Should have at least one token, but found none",
                    reader.getFile(),
                    new LineRange(lineNumber, lineNumber + 1)
            );

            reader.readLine();
            return;
        }

        keyword.setName(first.get().getValue());

        reader.readLine();
    }

    static String getLabel(LineReader reader, Tokens tokens, ErrorManager errors){
        Optional<Token> first = tokens.first();
        if(!first.isPresent()){
            int lineNumber = reader.getCurrent().getNumber();
            errors.registerInternalError(
                    "Not expecting an empty token",
                    reader.getFile(),
                    new LineRange(lineNumber, lineNumber + 1)
            );

            return "";
        }

        return first.get().getValue();
    }
}
