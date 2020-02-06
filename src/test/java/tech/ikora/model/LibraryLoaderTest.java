package tech.ikora.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import tech.ikora.builder.LibraryLoader;
import tech.ikora.error.ErrorManager;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryLoaderTest {
    @Test
    void testLibraryImports() throws JsonProcessingException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ErrorManager errors = new ErrorManager();
        LibraryResources resources = LibraryLoader.load(errors);
        assertTrue(errors.isEmpty());

        final Keyword builtin = resources.findKeyword(Token.fromString("Call Method"));
        assertNotNull(builtin);

        final Keyword external = resources.findKeyword(Token.fromString("Page Should Not Contain"));
        assertNotNull(external);
    }
}
