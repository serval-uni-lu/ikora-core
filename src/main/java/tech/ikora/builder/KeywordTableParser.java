package tech.ikora.builder;

import tech.ikora.error.ErrorManager;
import tech.ikora.model.NodeTable;
import tech.ikora.model.Tokens;
import tech.ikora.model.UserKeyword;

import java.io.IOException;

class KeywordTableParser {
    private KeywordTableParser() {}

    public static NodeTable<UserKeyword> parse(LineReader reader, Tokens blockTokens, DynamicImports dynamicImports, ErrorManager errors) throws IOException {
        NodeTable<UserKeyword> keywordTable = new NodeTable<>();
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
