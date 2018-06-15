package lu.uni.serval.robotframework.parser;

import lu.uni.serval.robotframework.model.KeywordTable;
import lu.uni.serval.robotframework.model.TestCaseTable;

import java.io.BufferedReader;
import java.io.IOException;

public class KeywordTableParser {
    private KeywordTableParser() {}

    static public String parse(BufferedReader bufferedReader, KeywordTable keywordTable) throws IOException {
        String line = bufferedReader.readLine();

        while(line != null){
            if(ParsingUtils.isBlock(line)){
                break;
            }

            if(line.isEmpty()){
                continue;
            }

            line = UserKeywordParser.parse(bufferedReader, line, keywordTable);
        }

        return line;
    }
}
