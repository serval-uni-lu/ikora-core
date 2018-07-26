package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.KeywordTable;
import lu.uni.serval.robotframework.model.UserKeyword;

import java.io.IOException;

public class KeywordTableParser {
    private KeywordTableParser() {}

    static public KeywordTable parse(LineReader reader) throws IOException {
        KeywordTable keywordTable = new KeywordTable();

        reader.readLine();

        while(reader.getCurrent().isValid() && !Utils.isBlock(reader.getCurrent().getText())){
            if(Utils.ignore(reader.getCurrent())){
                reader.readLine();
                continue;
            }

            UserKeyword userKeyword = UserKeywordParser.parse(reader);
            keywordTable.add(userKeyword);
        }

        return keywordTable;
    }
}
