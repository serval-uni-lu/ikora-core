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
package lu.uni.serval.ikora.core.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimeValueTest {

    @Test
    void testGetTimeInSeconds() {
        TimeValue time = new TimeValue(TimeValue.Sign.POSITIVE, 1, 1, 1, 1, 1);
        assertEquals(90061.001, time.getTimeInSeconds());
    }

    @Test
    void testGetTimeInMilliseconds() {
        TimeValue time = new TimeValue(TimeValue.Sign.POSITIVE, 1, 1, 1, 1, 1);
        assertEquals(90061001, time.getTimeInMilliseconds());
    }

    @Test
    void testIsValidWithNumeric() {
        assertTrue(TimeValue.isValid(Token.fromString("10")));
        assertTrue(TimeValue.isValid(Token.fromString("-1")));
        assertTrue(TimeValue.isValid(Token.fromString("50.53")));
        assertTrue(TimeValue.isValid(Token.fromString("-42.1")));
        assertTrue(TimeValue.isValid(Token.fromString("0")));

        assertFalse(TimeValue.isValid(Token.fromString("-10AB")));
    }

    @Test
    void testIsValidWithString(){
        assertTrue(TimeValue.isValid(Token.fromString("1 min 30 secs")));
        assertTrue(TimeValue.isValid(Token.fromString("1.5 minutes")));
        assertTrue(TimeValue.isValid(Token.fromString("90 s")));
        assertTrue(TimeValue.isValid(Token.fromString("1 day 2 hours 3 minutes 4 seconds 5 milliseconds")));
        assertTrue(TimeValue.isValid(Token.fromString("1d 2h 3m 4s 5ms")));
        assertTrue(TimeValue.isValid(Token.fromString("- 10 seconds")));
    }

    @Test
    void testIsValidWithTimer(){
        assertTrue(TimeValue.isValid(Token.fromString("00:00:01")));
        assertTrue(TimeValue.isValid(Token.fromString("01:02:03")));
        assertTrue(TimeValue.isValid(Token.fromString("1:00:00")));
        assertTrue(TimeValue.isValid(Token.fromString("100:00:00")));
        assertTrue(TimeValue.isValid(Token.fromString("00:02")));
        assertTrue(TimeValue.isValid(Token.fromString("42:00")));
        assertTrue(TimeValue.isValid(Token.fromString("00:01:02.003")));
        assertTrue(TimeValue.isValid(Token.fromString("00:01.5")));
        assertTrue(TimeValue.isValid(Token.fromString("-01:02.345")));
    }
}
