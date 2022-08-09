/*
 *
 *     Copyright © 2019 - 2022 University of Luxembourg
 *
 *     Licensed under the Apache License, Version 2.0 (the "License")
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package lu.uni.serval.ikora.core.parser;

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
