package lu.uni.serval.robotframework.parser;

import lu.uni.serval.robotframework.model.TestCaseTable;

import java.io.BufferedReader;
import java.io.IOException;

public class TestCaseTableParser {
    private TestCaseTableParser() {}

    static public String parse(BufferedReader bufferedReader, TestCaseTable testCaseTable) throws IOException {
        String line = bufferedReader.readLine();

        while(line != null){
            if(ParsingUtils.isBlock(line)){
                break;
            }

            if(line.isEmpty()){
                continue;
            }

            line = TestCaseParser.parse(bufferedReader, line, testCaseTable);
        }

        return line;
    }
}
