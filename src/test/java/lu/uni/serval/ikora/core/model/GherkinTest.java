package lu.uni.serval.ikora.core.model;

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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GherkinTest {
    @Test
    void testStringConstructorWithValid(){
        Gherkin gherkinGiven = new Gherkin(Token.fromString("Given that this is a gherkin test."));
        Gherkin gherkinWhen = new Gherkin(Token.fromString("When given a string to play with."));
        Gherkin gherkinThen = new Gherkin(Token.fromString("Then it should work like a charm."));
        Gherkin gherkinAnd = new Gherkin(Token.fromString("And make me super happy."));
        Gherkin gherkinBut = new Gherkin(Token.fromString("But only if the test is good."));

        assertEquals(Gherkin.Type.GIVEN, gherkinGiven.getType());
        assertEquals(Gherkin.Type.WHEN, gherkinWhen.getType());
        assertEquals(Gherkin.Type.THEN, gherkinThen.getType());
        assertEquals(Gherkin.Type.AND, gherkinAnd.getType());
        assertEquals(Gherkin.Type.BUT, gherkinBut.getType());
    }

    @Test
    void testStringConstructorWithInvalid(){
        Gherkin gherkinInvalid = new Gherkin(Token.fromString("If there is no type it should be none"));
        assertEquals(Gherkin.Type.NONE, gherkinInvalid.getType());
    }

    @Test
    void testStaticConstructor(){
        Gherkin gherkinNone = Gherkin.none();
        assertEquals(Gherkin.Type.NONE, gherkinNone.getType());
    }
}
