package org.ukwikora.compiler;

import org.ukwikora.model.StatementTable;
import org.ukwikora.model.UserKeyword;

import java.io.IOException;

class KeywordTableParser {
    private KeywordTableParser() {}

    static public StatementTable<UserKeyword> parse(LineReader reader) throws IOException {
        StatementTable<UserKeyword> statementTable = new StatementTable<>();

        reader.readLine();

        while(reader.getCurrent().isValid() && !LexerUtils.isBlock(reader.getCurrent().getText())){
            if(reader.getCurrent().ignore()){
                reader.readLine();
                continue;
            }

            UserKeyword userKeyword = UserKeywordParser.parse(reader);
            statementTable.add(userKeyword);
        }

        return statementTable;
    }
}
