package lu.uni.serval.ikora.core.model;

import lu.uni.serval.ikora.core.builder.LibraryLoader;
import lu.uni.serval.ikora.core.error.ErrorManager;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryLoaderTest {
    @Test
    void testLibraryImports() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ErrorManager errors = new ErrorManager();
        LibraryResources resources = LibraryLoader.load(errors);
        assertTrue(errors.isEmpty());

        final Keyword builtin = resources.findKeyword(Token.fromString("Call Method"));
        assertNotNull(builtin);

        final Keyword external = resources.findKeyword(Token.fromString("Page Should Not Contain"));
        assertNotNull(external);
    }
}
