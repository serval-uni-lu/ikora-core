package lu.uni.serval.robotframework.model;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class GitRepository {
    private Git git;

    public GitRepository(String url, String localPath)  {
        try {
            clone(url, localPath);
        } catch (GitAPIException e) {
            e.printStackTrace();
            git = null;
        }
    }

    public TestCase findTestCase(String source, String name) {
        return null;
    }

    public void checkout(LocalDateTime dateTime) {

    }

    private void clone(String url, String localPath) throws GitAPIException {

        String name = FilenameUtils.getBaseName(url);
        File parentFolder = new File(localPath);
        File gitFolder = new File(parentFolder, name);

        this.git = Git.cloneRepository()
                .setURI(url)
                .setDirectory(gitFolder)
                .call();
    }
}
