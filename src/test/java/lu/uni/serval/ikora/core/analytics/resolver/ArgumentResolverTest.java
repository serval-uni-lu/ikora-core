package lu.uni.serval.ikora.core.analytics.resolver;

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

import lu.uni.serval.ikora.core.builder.BuildResult;
import lu.uni.serval.ikora.core.builder.Builder;
import lu.uni.serval.ikora.core.model.KeywordCall;
import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.model.UserKeyword;
import lu.uni.serval.ikora.core.types.KeywordType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentResolverTest {
    @Test
    void testWithMultipleKeywordsAsArgument(){
        final String code = "*** Keywords ***\n" +
                "multiple keywords\n" +
                "\tRun Keywords\tLog\t1\tAND\tLog\t2\tAND\tLog\t3\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();
        final UserKeyword keyword = project.findUserKeyword(Token.fromString("multiple keywords")).iterator().next();
        final KeywordCall call = (KeywordCall) keyword.getStep(0);

        assertEquals(3, call.getArgumentList().size());
        assertEquals(KeywordType.class, call.getArgumentList().get(0).getType().getClass());
        assertEquals(KeywordType.class, call.getArgumentList().get(1).getType().getClass());
        assertEquals(KeywordType.class, call.getArgumentList().get(2).getType().getClass());

        assertEquals("Log", call.getArgumentList().get(0).getName());
        assertEquals("Log", call.getArgumentList().get(1).getName());
        assertEquals("Log", call.getArgumentList().get(2).getName());

        assertEquals("1", ((KeywordCall)call.getArgumentList().get(0).getDefinition()).getArgumentList().get(0).getName());
        assertEquals("2", ((KeywordCall)call.getArgumentList().get(1).getDefinition()).getArgumentList().get(0).getName());
        assertEquals("3", ((KeywordCall)call.getArgumentList().get(2).getDefinition()).getArgumentList().get(0).getName());
    }
}
