package org.ukwikora.builder;

import org.ukwikora.model.NodeTable;
import org.ukwikora.model.UserKeyword;

class KeywordTableParser {
    private KeywordTableParser() {}

    public static NodeTable<UserKeyword> parse(LineReader reader, DynamicImports dynamicImports) throws Exception {
        NodeTable<UserKeyword> nodeTable = new NodeTable<>();

        reader.readLine();

        while(reader.getCurrent().isValid() && !LexerUtils.isBlock(reader.getCurrent().getText())){
            if(reader.getCurrent().ignore()){
                reader.readLine();
                continue;
            }

            UserKeyword userKeyword = UserKeywordParser.parse(reader, dynamicImports);
            nodeTable.add(userKeyword);
        }

        return nodeTable;
    }
}
