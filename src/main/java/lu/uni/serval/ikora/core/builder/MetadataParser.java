package lu.uni.serval.ikora.core.builder;

import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.error.ErrorMessages;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.utils.StringUtils;
import lu.uni.serval.ikora.core.utils.TokenUtils;

import java.util.Iterator;

public class MetadataParser {
    private MetadataParser() {}

    public static boolean is(Token label){
        return StringUtils.matchesIgnoreCase(label, "\\[metadata\\]");
    }

    public static Metadata parse(LineReader reader, Token label, Iterator<Token> tokenIterator, ErrorManager errors) {
        final Token key = tokenIterator.hasNext() ? tokenIterator.next() : Token.empty();
        final Value value = tokenIterator.hasNext() ? ValueParser.parseValue(tokenIterator.next()) : new Literal(Token.empty());

        if(tokenIterator.hasNext()){
            errors.registerSyntaxError(
                    reader.getSource(),
                    ErrorMessages.TOO_MANY_METADATA_ARGUMENTS,
                    Range.fromTokens(TokenUtils.accumulate(tokenIterator), reader.getCurrent())
            );
        }

        return new Metadata(label, key, value);
    }
}
