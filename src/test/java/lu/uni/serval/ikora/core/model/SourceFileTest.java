package lu.uni.serval.ikora.core.model;

import lu.uni.serval.ikora.core.builder.BuildResult;
import lu.uni.serval.ikora.core.builder.Builder;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SourceFileTest {
    @Test
    void testBuiltinImport(){
        final String code =
                "*** Test Cases ***\n" +
                "Test with BuiltIn call\n" +
                "\tLog\tHello there!\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final Optional<SourceFile> sourceFile = project.getSourceFile("<IN_MEMORY>");
        assertTrue(sourceFile.isPresent());

        assertTrue(sourceFile.get().isImportLibrary(Library.BUILTIN));
    }

    @Test
    void testLibraryImport(){
        final String code =
                "*** Settings ***\n" +
                "Library    Selenium2Library\n" +
                "*** Test Cases ***\n" +
                "Test with BuiltIn call\n" +
                "    Title Should Be    Login Page\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final Optional<SourceFile> sourceFile = project.getSourceFile("<IN_MEMORY>");
        assertTrue(sourceFile.isPresent());

        final TestCase test = project.getTestCases().iterator().next();

        assertTrue(sourceFile.get().isImportLibrary(test.getSteps().get(0).getKeywordCall().get().getKeyword().get().getLibraryName()));
    }

    @Test
    void testMissingLibraryImport(){
        final String code =
            "*** Test Cases ***\n" +
            "Test with BuiltIn call\n" +
            "    Title Should Be    Login Page\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final Optional<SourceFile> sourceFile = project.getSourceFile("<IN_MEMORY>");
        assertTrue(sourceFile.isPresent());

        assertFalse(sourceFile.get().isImportLibrary("Selenium2Library"));
    }

}