package lu.uni.serval.ikora.core.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserKeywordTest {
    @Test
    void testInitializeFromToken(){
        final Token name = Token.fromString("Some keyword");
        final UserKeyword keyword = new UserKeyword(name);

        assertEquals(name.getText(), keyword.getName());
        assertEquals(0, keyword.getParameters().size());
        assertFalse(keyword.getDocumentation().isPresent());
    }

    @Test
    void testSetScalarParameter(){
        final Token name = Token.fromString("Some keyword with parameters");
        final UserKeyword keyword = new UserKeyword(name);
        final Token scalar = Token.fromString("${Scalar}");

        final NodeList<Variable> parameters = new NodeList<>();
        parameters.add(new ScalarVariable(scalar));

        keyword.setParameters(parameters);

        assertEquals(1, keyword.getParameters().size());
        assertTrue(keyword.getParameterByName(scalar).isPresent());
        assertEquals(scalar.getText(), keyword.getParameterByName(scalar).get().getName());
    }
}
