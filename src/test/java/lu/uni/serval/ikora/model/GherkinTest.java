package lu.uni.serval.ikora.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GherkinTest {
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
