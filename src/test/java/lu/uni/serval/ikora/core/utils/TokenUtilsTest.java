package lu.uni.serval.ikora.core.utils;

import lu.uni.serval.ikora.core.model.Token;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenUtilsTest {
    @Test
    void testExtractEqualSign(){
        final Token equalSign = TokenUtils.extractEqualSign(Token.fromString("\t${value}="));
        assertEquals("=", equalSign.getText());
    }
}