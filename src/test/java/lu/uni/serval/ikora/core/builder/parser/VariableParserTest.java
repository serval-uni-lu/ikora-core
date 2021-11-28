package lu.uni.serval.ikora.core.builder.parser;

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

import lu.uni.serval.ikora.core.model.*;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class VariableParserTest {
    @Test
    void testParseScalarName(){
        Token scalar = Token.fromString("${scalar}");
        Optional<Variable> variable = VariableParser.parse(scalar);
        assertTrue(variable.isPresent());
        assertEquals(variable.get().getClass(), ScalarVariable.class);
    }

    @Test
    void testParseListName(){
        Token list = Token.fromString("@{list}");
        Optional<Variable> variable = VariableParser.parse(list);
        assertTrue(variable.isPresent());
        assertEquals(variable.get().getClass(), ListVariable.class);
    }

    @Test
    void testParseDictionaryName(){
        Token dictionary = Token.fromString("&{dictionary}");
        Optional<Variable> variable = VariableParser.parse(dictionary);
        assertTrue(variable.isPresent());
        assertEquals(variable.get().getClass(), DictionaryVariable.class);
    }

    @Test
    void testParseNonVariableName(){
        Token random = Token.fromString("not a variable");
        Optional<Variable> variable = VariableParser.parse(random);
        assertFalse(variable.isPresent());
    }

    @Test
    void testParseNonVariableContainingAVariable(){
        Token random = Token.fromString("not a variable containing a ${scalar}");
        Optional<Variable> variable = VariableParser.parse(random);
        assertFalse(variable.isPresent());
    }
}
