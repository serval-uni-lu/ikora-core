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

class LineTest {
    @Test
    void checkSimpleLine(){
        Line simple = new Line("simple line", 4, false, false);

        assertTrue(simple.isValid());
        assertEquals("simple line", simple.getText());
        assertEquals(4, simple.getNumber());
        assertFalse(simple.isComment());
        assertFalse(simple.isEmpty());
        assertFalse(simple.ignore());
    }

    @Test
    void checkInvalidLine(){
        Line invalid = new Line(null, 3, false, true);

        assertFalse(invalid.isValid());
        assertNull(invalid.getText());
        assertEquals(3, invalid.getNumber());
        assertFalse(invalid.isComment());
        assertTrue(invalid.isEmpty());
        assertTrue(invalid.ignore());
    }

    @Test
    void checkEmptyLine(){
        Line invalid = new Line("", 12, false, true);

        assertTrue(invalid.isValid());
        assertEquals("", invalid.getText());
        assertEquals(12, invalid.getNumber());
        assertFalse(invalid.isComment());
        assertTrue(invalid.isEmpty());
        assertTrue(invalid.ignore());
    }
}
