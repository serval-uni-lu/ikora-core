package lu.uni.serval.ikora.core.libraries.builtin.variables;

/*-
 * #%L
 * Ikora Core
 * %%
 * Copyright (C) 2019 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import lu.uni.serval.ikora.core.model.Token;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestStatusTest {
    @Test
    void checkVariableResolution(){
        TestStatus testStatus = new TestStatus();

        assertTrue(testStatus.matches(Token.fromString("${TEST STATUS}")));
        assertTrue(testStatus.matches(Token.fromString("${TEST_STATUS}")));
        assertTrue(testStatus.matches(Token.fromString("${test status}")));

        assertFalse(testStatus.matches(Token.fromString("${TEST CASE}")));
        assertFalse(testStatus.matches(Token.fromString("&{TEST STATUS}")));
        assertFalse(testStatus.matches(Token.fromString("TEST STATUS")));
    }
}
