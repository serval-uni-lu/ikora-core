package lu.uni.serval.ikora.core.builder;

import lu.uni.serval.ikora.core.Helpers;
import lu.uni.serval.ikora.core.error.ErrorManager;
import lu.uni.serval.ikora.core.model.NodeList;
import lu.uni.serval.ikora.core.model.Token;
import lu.uni.serval.ikora.core.model.Variable;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentsParserTest {
    @Test
    void testSingleArgument() throws IOException {
        final String text = "\t[arguments]\t${arg1}";
        final LineReader reader = Helpers.getLineReader(text);
        final Iterator<Token> tokenIterator = Helpers.getTokenIterator(reader);
        final ErrorManager errors = new ErrorManager();

        final NodeList<Variable> arguments = ArgumentsParser.parse(reader, tokenIterator.next(), tokenIterator, errors);

        assertTrue(errors.isEmpty());
        assertEquals(1, arguments.size());
        assertTrue(arguments.get(0).matches(Token.fromString("${arg1}")));
    }

    @Test
    void testMultipleArguments() throws IOException {
        final String text = "\t[arguments]\t${arg1}\t@{arg2}\t&{arg3}";
        final LineReader reader = Helpers.getLineReader(text);
        final Iterator<Token> tokenIterator = Helpers.getTokenIterator(reader);
        final ErrorManager errors = new ErrorManager();

        final NodeList<Variable> arguments = ArgumentsParser.parse(reader, tokenIterator.next(), tokenIterator, errors);

        assertTrue(errors.isEmpty());
        assertEquals(3, arguments.size());
        assertTrue(arguments.get(0).matches(Token.fromString("${arg1}")));
        assertTrue(arguments.get(1).matches(Token.fromString("@{arg2}")));
        assertTrue(arguments.get(2).matches(Token.fromString("&{arg3}")));
    }

    @Test
    void testNoArgument() throws IOException {
        final String text = "\t[arguments]";
        final LineReader reader = Helpers.getLineReader(text);
        final Iterator<Token> tokenIterator = Helpers.getTokenIterator(reader);
        final ErrorManager errors = new ErrorManager();

        final NodeList<Variable> arguments = ArgumentsParser.parse(reader, tokenIterator.next(), tokenIterator, errors);

        assertTrue(errors.isEmpty());
        assertEquals(0, arguments.size());
    }

    @Test
    void testInvalidArgument() throws IOException {
        final String text = "\t[arguments]\t{arg1}";
        final LineReader reader = Helpers.getLineReader(text);
        final Iterator<Token> tokenIterator = Helpers.getTokenIterator(reader);
        final ErrorManager errors = new ErrorManager();

        final NodeList<Variable> arguments = ArgumentsParser.parse(reader, tokenIterator.next(), tokenIterator, errors);

        assertFalse(errors.isEmpty());
        assertEquals(0, arguments.size());
    }
}