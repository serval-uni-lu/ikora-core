package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.ElementTable;
import lu.uni.serval.robotframework.model.UserKeyword;

import java.io.IOException;

public class KeywordTableParser {
    private KeywordTableParser() {}

    static public ElementTable<UserKeyword> parse(LineReader reader) throws IOException {
        ElementTable<UserKeyword> elementTable = new ElementTable<>();

        reader.readLine();

        while(reader.getCurrent().isValid() && !Utils.isBlock(reader.getCurrent().getText())){
            if(Utils.ignore(reader.getCurrent())){
                reader.readLine();
                continue;
            }

            UserKeyword userKeyword = UserKeywordParser.parse(reader);
            elementTable.add(userKeyword);
        }

        return elementTable;
    }
}
