package org.ukwikora.builder;

import org.ukwikora.error.Error;
import org.ukwikora.model.NodeTable;
import org.ukwikora.model.UserKeyword;

import java.io.IOException;
import java.util.List;

class KeywordTableParser {
    private KeywordTableParser() {}

    public static NodeTable<UserKeyword> parse(LineReader reader, DynamicImports dynamicImports, List<Error> errors) throws IOException {
        NodeTable<UserKeyword> nodeTable = new NodeTable<>();

        reader.readLine();

        while(reader.getCurrent().isValid() && !LexerUtils.isBlock(reader.getCurrent().getText())){
            if(reader.getCurrent().ignore()){
                reader.readLine();
                continue;
            }

            UserKeyword userKeyword = UserKeywordParser.parse(reader, dynamicImports, errors);
            nodeTable.add(userKeyword);
        }

        return nodeTable;
    }
}
