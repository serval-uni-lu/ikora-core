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
package lu.uni.serval.ikora.core.utils;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {
    @Test
    void testCountLines(){
        String emptyBlock = "\n";
        String trailingBlock = "Trailing\n";
        String forwardBlock = "\nForward";
        String bothSideBlock = "\nBothSides\n";
        String twoLinesBlock = "Line1\nLine2";

        assertEquals(0, StringUtils.countLines(emptyBlock));
        assertEquals(1, StringUtils.countLines(trailingBlock));
        assertEquals(1, StringUtils.countLines(forwardBlock));
        assertEquals(1, StringUtils.countLines(bothSideBlock));
        assertEquals(2, StringUtils.countLines(twoLinesBlock));
    }

    @Test
    void testBeautifyName(){
        assertEquals("Lower", StringUtils.toBeautifulName("lower"));
        assertEquals("Upper", StringUtils.toBeautifulName("UPPER"));
        assertEquals("Test Lower Underline", StringUtils.toBeautifulName("test_lower_underline"));
        assertEquals("Test Upper Underline", StringUtils.toBeautifulName("TEST_UPPER_UNDERLINE"));
        assertEquals("Test Lower Dash", StringUtils.toBeautifulName("test-lower-dash"));
        assertEquals("Test Upper Dash", StringUtils.toBeautifulName("TEST-UPPER-DASH"));
        assertEquals("Test Trimming", StringUtils.toBeautifulName("test_trimming_"));
        assertEquals("Test Trimming", StringUtils.toBeautifulName("_test_trimming_"));
        assertEquals("Test Trimming", StringUtils.toBeautifulName("_test_trimming"));
    }

    @Test
    void testBeautifyUrl(){
        assertEquals("page-1.html", StringUtils.toBeautifulUrl("PAGE-1_", "html"));
        assertEquals("page-1.html", StringUtils.toBeautifulUrl("Page 1", "html"));
        assertEquals("page-1.html", StringUtils.toBeautifulUrl("Page1", "html"));
        assertEquals("page-1.html", StringUtils.toBeautifulUrl("Page_1", "html"));
    }

    @Test
    void testLineTruncate(){
        assertEquals("Some random line", StringUtils.lineTruncate("Some random line", -1));
        assertEquals("", StringUtils.lineTruncate("Some random line", 0));
        assertEquals(".", StringUtils.lineTruncate("Some random line", 1));
        assertEquals("..", StringUtils.lineTruncate("Some random line", 2));
        assertEquals("...", StringUtils.lineTruncate("Some random line", 3));
        assertEquals("Some ra...", StringUtils.lineTruncate("Some random line", 10));
        assertEquals("Some random ...", StringUtils.lineTruncate("Some random line", 15));
        assertEquals("Some random line", StringUtils.lineTruncate("Some random line", 16));
    }

    @Test
    void testTrimRight(){
        assertEquals("${variable}", StringUtils.trimRight("${variable}=", "="));
        assertEquals("${variable}", StringUtils.trimRight("${variable} =", " ="));
        assertEquals("=${variable}", StringUtils.trimRight("=${variable}", "="));
        assertEquals("${variable}=", StringUtils.trimRight("${variable}=", "<"));
    }

    @Test
    void testTrimLeft(){
        assertEquals("{variable}", StringUtils.trimLeft("${variable}", "$"));
        assertEquals("{variable}", StringUtils.trimLeft(" ${variable}", " $"));
        assertEquals("{variable}$", StringUtils.trimLeft("{variable}$", "="));
        assertEquals("${variable}", StringUtils.trimLeft("${variable}", "<"));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "value:'':value",
            "=value:'':value",
            "param=value:param:value",
            "param=:param:''",
            "param\\=value:'':param\\=value",
            "'':'':''"},
            delimiter = ':'
    )
    void testSplitEquals(String text, String expectedLeft, String expectedRight){
        final Pair<String, String> split = StringUtils.splitEqual(text);

        assertEquals(expectedLeft, split.getLeft());
        assertEquals(expectedRight, split.getRight());
    }
}
