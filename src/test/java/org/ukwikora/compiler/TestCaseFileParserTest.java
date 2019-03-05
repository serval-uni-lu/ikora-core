package org.ukwikora.compiler;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestCaseFileParserTest {
    @Test
    public void checkSettingsBlockResolution(){
        assertTrue(TestCaseFileParser.isSettings("***Settings***"));
        assertTrue(TestCaseFileParser.isSettings("***Setting***"));
        assertTrue(TestCaseFileParser.isSettings("*** settings ***"));
        assertTrue(TestCaseFileParser.isSettings("*** Setting ***  "));
        assertTrue(TestCaseFileParser.isSettings("*** Setting"));
        assertFalse(TestCaseFileParser.isSettings("***Wrong***"));
    }

    @Test
    public void checkKeywordsBlockResolution(){
        assertTrue(TestCaseFileParser.isKeywords("***Keywords***"));
        assertTrue(TestCaseFileParser.isKeywords("***Keyword***"));
        assertTrue(TestCaseFileParser.isKeywords("*** keywords ***"));
        assertTrue(TestCaseFileParser.isKeywords("*** Keyword ***"));
        assertTrue(TestCaseFileParser.isKeywords("*** Keywords"));
        assertFalse(TestCaseFileParser.isKeywords("***Wrong***"));
    }

    @Test
    public void checkTestCasesBlockResolution(){
        assertTrue(TestCaseFileParser.isTestCases("***Test Cases***"));
        assertTrue(TestCaseFileParser.isTestCases("***Test Case***"));
        assertTrue(TestCaseFileParser.isTestCases("*** test cases ***"));
        assertTrue(TestCaseFileParser.isTestCases("*** Test Case ***"));
        assertTrue(TestCaseFileParser.isTestCases("*** Test Cases"));
        assertFalse(TestCaseFileParser.isTestCases("***Wrong***"));
    }

    @Test
    public void checkVariableBlockResolution(){
        assertTrue(TestCaseFileParser.isVariable("***Variables***"));
        assertTrue(TestCaseFileParser.isVariable("***Variable***"));
        assertTrue(TestCaseFileParser.isVariable("*** variables ***"));
        assertTrue(TestCaseFileParser.isVariable("*** Variable ***"));
        assertTrue(TestCaseFileParser.isVariable("*** Variables"));
        assertFalse(TestCaseFileParser.isVariable("***Wrong***"));
    }
}