package lu.uni.serval.ikora.builder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SourceFileParserTest {
    @Test
    void checkSettingsBlockResolution(){
        assertTrue(SourceFileParser.isSettings("***Settings***"));
        assertTrue(SourceFileParser.isSettings("***Setting***"));
        assertTrue(SourceFileParser.isSettings("*** settings ***"));
        assertTrue(SourceFileParser.isSettings("*** Setting ***  "));
        assertTrue(SourceFileParser.isSettings("*** Setting ***\t"));
        assertTrue(SourceFileParser.isSettings("*** Setting"));
        assertFalse(SourceFileParser.isSettings("***Wrong***"));
    }

    @Test
    void checkKeywordsBlockResolution(){
        assertTrue(SourceFileParser.isKeywords("***Keywords***"));
        assertTrue(SourceFileParser.isKeywords("***Keyword***"));
        assertTrue(SourceFileParser.isKeywords("*** keywords ***"));
        assertTrue(SourceFileParser.isKeywords("*** Keyword ***"));
        assertTrue(SourceFileParser.isKeywords("*** Keywords"));
        assertFalse(SourceFileParser.isKeywords("***Wrong***"));
    }

    @Test
    void checkTestCasesBlockResolution(){
        assertTrue(SourceFileParser.isTestCases("***Test Cases***"));
        assertTrue(SourceFileParser.isTestCases("***Test Case***"));
        assertTrue(SourceFileParser.isTestCases("*** test cases ***"));
        assertTrue(SourceFileParser.isTestCases("*** Test Case ***"));
        assertTrue(SourceFileParser.isTestCases("*** Test Cases"));
        assertFalse(SourceFileParser.isTestCases("***Wrong***"));
    }

    @Test
    void checkVariableBlockResolution(){
        assertTrue(SourceFileParser.isVariable("***Variables***"));
        assertTrue(SourceFileParser.isVariable("***Variable***"));
        assertTrue(SourceFileParser.isVariable("*** variables ***"));
        assertTrue(SourceFileParser.isVariable("*** Variable ***"));
        assertTrue(SourceFileParser.isVariable("*** Variables"));
        assertFalse(SourceFileParser.isVariable("***Wrong***"));
    }
}