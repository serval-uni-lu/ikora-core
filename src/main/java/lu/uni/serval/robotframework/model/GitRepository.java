package lu.uni.serval.robotframework.model;

import lu.uni.serval.robotframework.compiler.Compiler;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

public class GitRepository {
    private Git git;
    private String url;
    private File localFolder;
    private String name;

    private Project project;

    public GitRepository(String url, String localPath)  {
        this.url = url;
        this.name = FilenameUtils.getBaseName(url);

        File parentFolder = new File(localPath);
        this.localFolder = new File(parentFolder, name);
    }

    public String getName() {
        return name;
    }

    public Project getProject() {
        return project;
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
        Pair<String, LocalDateTime> commit = getMostRecentCommit(dateTime);
        checkout(commit.getKey());
    }

    public void checkout(String commitId){
        try {
            if(git == null){
                cloneRepository();
            }

            Ref ref = git.checkout().setName(commitId).call();

            project = Compiler.compile(localFolder.getAbsolutePath());

            project.setGitUrl(url);
            project.setCommitId(commitId);
            project.setDateTime(getCommitDate(ref.getObjectId()));

        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    private void cloneRepository() throws GitAPIException {
        git = Git.cloneRepository()
                .setURI(url)
                .setDirectory(localFolder)
                .call();
    }

    public Pair<String, LocalDateTime> getMostRecentCommit(LocalDateTime dateTime){
        try {
            if(git == null){
                cloneRepository();
            }

            Map<String, LocalDateTime> map = getRevisions();

            Map.Entry<String, LocalDateTime> mostRecentCommit = null;

            for (Map.Entry<String, LocalDateTime> pair: map.entrySet()) {
                if(mostRecentCommit == null && pair.getValue().isBefore(dateTime)){
                    mostRecentCommit = pair;
                }else if (mostRecentCommit != null && pair.getValue().isBefore(dateTime)
                        && pair.getValue().isAfter(mostRecentCommit.getValue())){
                    mostRecentCommit = pair;
                }
            }

            if(mostRecentCommit == null){
                return null;
            }

            return ImmutablePair.of(mostRecentCommit.getKey(), mostRecentCommit.getValue());
        }
        catch (GitAPIException e){
            e.printStackTrace();
        }

        return null;
    }

    public Map<String, LocalDateTime > getRevisions() {
        Map<String, LocalDateTime> commitMap = new HashMap<>();

        try {
            if(git == null){
                cloneRepository();
            }

            try {
                for (RevCommit revCommit : git.log().call()) {
                    Instant instant = Instant.ofEpochSecond(revCommit.getCommitTime());
                    LocalDateTime commitDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                    commitMap.put(revCommit.getName(),commitDate);
                }
            } catch (GitAPIException e) {
                e.printStackTrace();
            }

            return commitMap;
        }
            catch (GitAPIException e){
            e.printStackTrace();
        }

        return commitMap;
    }

    private LocalDateTime getCommitDate(ObjectId commitId){
        LocalDateTime commitDate = null;

        try {
            if(git == null){
                cloneRepository();
            }

            RevWalk revWalk = new RevWalk(git.getRepository());
            RevCommit revCommit = revWalk.parseCommit(commitId);

            commitDate = LocalDateTime.from(Instant.ofEpochSecond(revCommit.getCommitTime()));
        } catch (IOException|GitAPIException e) {
            e.printStackTrace();
        }

        return commitDate;
    }
}
