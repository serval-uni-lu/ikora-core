package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.KeywordTable;

import java.io.LineNumberReader;
import java.io.IOException;

public class KeywordTableParser {
    private KeywordTableParser() {}

    static public Line parse(LineNumberReader reader, KeywordTable keywordTable) throws IOException {
        Line line = Line.getNextLine(reader);

        while(line.isValid()){
            if(ParsingUtils.isBlock(line.getText())){
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
