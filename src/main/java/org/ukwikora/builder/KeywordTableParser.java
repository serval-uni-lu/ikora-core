package org.ukwikora.builder;

import org.ukwikora.model.StatementTable;
import org.ukwikora.model.UserKeyword;

class KeywordTableParser {
    private KeywordTableParser() {}

    public static StatementTable<UserKeyword> parse(LineReader reader, DynamicImports dynamicImports) throws Exception {
        StatementTable<UserKeyword> statementTable = new StatementTable<>();

        reader.readLine();

        while(reader.getCurrent().isValid() && !LexerUtils.isBlock(reader.getCurrent().getText())){
            if(reader.getCurrent().ignore()){
                reader.readLine();
                continue;
            }

            UserKeyword userKeyword = UserKeywordParser.parse(reader, dynamicImports);
            statementTable.add(userKeyword);
        }

        return statementTable;
    }
}
