package lu.uni.serval.ikora.core.builder.resolver;

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