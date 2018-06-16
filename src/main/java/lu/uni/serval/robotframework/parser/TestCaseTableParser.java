package lu.uni.serval.robotframework.parser;

import lu.uni.serval.robotframework.model.TestCaseTable;

import java.io.LineNumberReader;
import java.io.IOException;

public class TestCaseTableParser {
    private TestCaseTableParser() {}

    static public String parse(LineNumberReader reader, TestCaseTable testCaseTable) throws IOException {
        String line = reader.readLine();

        while(line != null){
            if(ParsingUtils.isBlock(line)){
                break;
            }

            if(line.isEmpty()){
                continue;
            }

            line = TestCaseParser.parse(reader, line, testCaseTable);
        }

        return line;
    }
}
