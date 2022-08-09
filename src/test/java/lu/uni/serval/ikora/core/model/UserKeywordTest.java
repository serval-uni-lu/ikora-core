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

import lu.uni.serval.ikora.core.types.StringType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserKeywordTest {
    @Test
    void testInitializeFromToken(){
        final Token name = Token.fromString("Some keyword");
        final UserKeyword keyword = new UserKeyword(name);

        assertEquals(name.getText(), keyword.getName());
        assertEquals(0, keyword.getArguments().size());
        assertFalse(keyword.getDocumentation().isPresent());
    }

    @Test
    void testSetScalarParameter(){
        final Token name = Token.fromString("Some keyword with parameters");
        final UserKeyword keyword = new UserKeyword(name);
        final Token scalar = Token.fromString("${Scalar}");

        final NodeList<Argument> parameters = new NodeList<>();
        parameters.add(new Argument(new ScalarVariable(scalar), new StringType(scalar.getText()), 0));

        keyword.setArguments(parameters);

        assertEquals(1, keyword.getArguments().size());
        assertTrue(keyword.getParameterByName(scalar).isPresent());
        assertEquals(scalar.getText(), keyword.getParameterByName(scalar).get().getName());
    }
}
