package lu.uni.serval.robotframework.parser;

import lu.uni.serval.robotframework.model.KeywordTable;

import java.io.LineNumberReader;
import java.io.IOException;

public class KeywordTableParser {
    private KeywordTableParser() {}

    static public String parse(LineNumberReader reader, KeywordTable keywordTable) throws IOException {
        String line = reader.readLine();

        while(line != null){
            if(ParsingUtils.isBlock(line)){
                break;
            }

            if(line.isEmpty()){
                continue;
            }

            line = UserKeywordParser.parse(reader, line, keywordTable);
        }

        return line;
    }
}
