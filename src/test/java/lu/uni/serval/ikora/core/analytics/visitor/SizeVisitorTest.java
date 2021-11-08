package lu.uni.serval.ikora.core.analytics.visitor;

import lu.uni.serval.ikora.core.Helpers;
import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.SourceFile;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SizeVisitorTest {
    private static Project project;

    @BeforeAll
    static void setup(){
        project = Helpers.compileProject("robot/web-demo", true);
    }

    @Test
    void testFileWithResources(){
        final Optional<SourceFile> sourceFile = project.getSourceFile("gherkin_login.robot");
        assertTrue(sourceFile.isPresent());

        final SizeVisitor sizeVisitor = new SizeVisitor();
        sizeVisitor.visit(sourceFile.get(), new PathMemory());

        assertEquals(1, sizeVisitor.getResult().getTestCaseSize());
        assertEquals(8, sizeVisitor.getResult().getUserKeywordSize());
    }

    @Test
    void testFileWithoutResources(){
        final Optional<SourceFile> sourceFile = project.getSourceFile("resource.robot");
        assertTrue(sourceFile.isPresent());

        final SizeVisitor sizeVisitor = new SizeVisitor();
        sizeVisitor.visit(sourceFile.get(), new PathMemory());

        assertEquals(0, sizeVisitor.getResult().getTestCaseSize());
        assertEquals(7, sizeVisitor.getResult().getUserKeywordSize());
    }
}