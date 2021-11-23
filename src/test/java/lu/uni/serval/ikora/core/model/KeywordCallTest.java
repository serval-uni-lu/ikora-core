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

import lu.uni.serval.ikora.core.analytics.difference.Edit;
import lu.uni.serval.ikora.core.exception.MalformedVariableException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KeywordCallTest {
    @Test
    void testInitialization() throws MalformedVariableException {
        final Variable variable1 = Variable.create(Token.fromString("${variable1}"));
        final Variable variable2 = Variable.create(Token.fromString("&{variable2}"));
        final Literal literal = new Literal(Token.fromString("value"));

        final NodeList<Argument> arguments = new NodeList<>();
        arguments.add(new Argument(variable1));
        arguments.add(new Argument(variable2));
        arguments.add(new Argument(literal));

        final KeywordCall call = new KeywordCall(Token.fromString("Call"));
        call.setArgumentList(arguments);

        assertEquals("Call", call.getName());
        assertEquals(3, call.getArgumentList().size());
        assertEquals(Gherkin.Type.NONE, call.getGherkin().getType());

        assertEquals(call, variable1.getAstParent());
        assertEquals(call, variable2.getAstParent());
        assertEquals(call, literal.getAstParent());
    }

    @Test
    void testDifferenceWithChangeInArgumentType() throws MalformedVariableException {
        final KeywordCall call1 = new KeywordCall(Token.fromString("Call"));
        final NodeList<Argument> arguments1 = new NodeList<>();
        arguments1.add(new Argument(new Literal(Token.fromString("value1"))));
        arguments1.add(new Argument(new Literal(Token.fromString("value2"))));
        call1.setArgumentList(arguments1);

        final KeywordCall call2 = new KeywordCall(Token.fromString("Call"));
        final NodeList<Argument> arguments2 = new NodeList<>();
        arguments2.add(new Argument(Variable.create(Token.fromString("${variable}"))));
        arguments2.add(new Argument(new Literal(Token.fromString("value2"))));
        call2.setArgumentList(arguments2);

        final List<Edit> differences = call1.differences(call2);

        assertEquals(1, differences.size());
        assertEquals(Edit.Type.CHANGE_VALUE_TYPE, differences.get(0).getType());
    }
}
