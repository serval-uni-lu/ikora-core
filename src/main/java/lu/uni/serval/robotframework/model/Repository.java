package lu.uni.serval.robotframework.model;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.time.LocalDateTime;

public class Repository {
    private Git git;

    public Repository(String url, String localPath)  {
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
        this.git = Git.cloneRepository()
                .setURI(url)
                .setDirectory(new File(localPath))
                .call();
    }
}
