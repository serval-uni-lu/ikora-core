package lu.uni.serval.robotframework.model;

import lu.uni.serval.robotframework.compiler.Compiler;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.time.LocalDateTime;

public class GitRepository {
    private Git git;
    private File localFolder;
    private String name;
    Project project;

    public GitRepository(String url, String localPath)  {
        try {
            clone(url, localPath);
        } catch (GitAPIException e) {
            e.printStackTrace();
            git = null;
            localFolder = null;
        }
    }

    public String getName() {
        return name;
    }

    public TestCase findTestCase(String relativePath, String name) {
        File absolutePath = new File(localFolder, relativePath);

        TestCaseFile testCaseFile = project.getTestCaseFile(absolutePath);

        if(testCaseFile == null){
            return null;
        }

        return testCaseFile.getTestCase(name);
    }

    public void checkout(LocalDateTime dateTime) {
        git.checkout();
        project = Compiler.compile(localFolder.getAbsolutePath());
    }

    private void clone(String url, String localPath) throws GitAPIException {

        name = FilenameUtils.getBaseName(url);
        File parentFolder = new File(localPath);
        localFolder = new File(parentFolder, name);

        git = Git.cloneRepository()
                .setURI(url)
                .setDirectory(localFolder)
                .call();
    }
}
