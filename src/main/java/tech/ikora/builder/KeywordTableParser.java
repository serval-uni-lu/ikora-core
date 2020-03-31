package tech.ikora.builder;

import tech.ikora.error.ErrorManager;
import tech.ikora.model.SourceNodeTable;
import tech.ikora.model.Tokens;
import tech.ikora.model.UserKeyword;

import java.io.IOException;

class KeywordTableParser {
    private KeywordTableParser() {}

    public static SourceNodeTable<UserKeyword> parse(LineReader reader, Tokens blockTokens, DynamicImports dynamicImports, ErrorManager errors) throws IOException {
        SourceNodeTable<UserKeyword> keywordTable = new SourceNodeTable<>();
        keywordTable.setHeader(ParserUtils.parseHeaderName(reader, blockTokens, errors));

        while(reader.getCurrent().isValid() && !LexerUtils.isBlock(reader.getCurrent().getText())){
            if(reader.getCurrent().ignore()){
                reader.readLine();
                continue;
            }

            Tokens tokens = LexerUtils.tokenize(reader);

            UserKeyword userKeyword = UserKeywordParser.parse(reader, tokens, dynamicImports, errors);
            keywordTable.add(userKeyword);
        }

        return keywordTable;
    }
}
