package org.ukwikora.compiler;

import org.ukwikora.model.ElementTable;
import org.ukwikora.model.UserKeyword;

import java.io.IOException;

public class KeywordTableParser {
    private KeywordTableParser() {}

    static public ElementTable<UserKeyword> parse(LineReader reader) throws IOException {
        ElementTable<UserKeyword> elementTable = new ElementTable<>();

        reader.readLine();

        while(reader.getCurrent().isValid() && !Utils.isBlock(reader.getCurrent().getText())){
            if(reader.getCurrent().ignore()){
                reader.readLine();
                continue;
            }

            UserKeyword userKeyword = UserKeywordParser.parse(reader);
            elementTable.add(userKeyword);
        }

        return elementTable;
    }
}
