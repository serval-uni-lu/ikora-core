package lu.uni.serval.robotframework.parser;

import lu.uni.serval.robotframework.model.TestCaseTable;

import java.io.BufferedReader;
import java.io.IOException;

public class TestCasesParser {
    private TestCasesParser() {}

    static public String parse(BufferedReader bufferRead, TestCaseTable testCaseTable) throws IOException {
        String line;

        while((line = bufferRead.readLine()) != null){
            if(ParsingUtils.isBlock(line)){
                break;
            }

            String[] tokens = ParsingUtils.tokenizeLine(line);

            if(tokens.length == 0){
                continue;
            }
        }

        return line;
    }
}
