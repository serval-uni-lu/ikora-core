package lu.uni.serval.robotframework.model;

import lu.uni.serval.robotframework.compiler.Compiler;
import lu.uni.serval.utils.Configuration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

public class GitRepository {
    final static Logger logger = Logger.getLogger(GitRepository.class);

    private Git git;
    private String url;
    private String branch;
    private String username;
    private String password;
    private File localFolder;
    private String name;

    private Project project;

    public GitRepository(String url, String branch, String username, String password)  {
        this.url = url;
        this.branch = branch;
        this.username = username;
        this.password = password;

        this.name = FilenameUtils.getBaseName(url);

        File cache = Configuration.getInstance().getConfigurationFolder();
        this.localFolder = new File(cache, "git");

        if(!this.localFolder.exists()){
            this.localFolder.mkdir();
        }

        this.localFolder = new File(this.localFolder.getAbsolutePath(), name);
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
        logger.info("checkout and parse project for commit ID: " + commitId);
        try {
            if(git == null){
                cloneRepository();
            }

            Ref ref = git.checkout()
                    .setCreateBranch(true)
                    .setName(commitId)
                    .setStartPoint(commitId)
                    .call();

            project = Compiler.compile(localFolder.getAbsolutePath());

            project.setGitUrl(url);
            project.setCommitId(commitId);
            project.setDateTime(getCommitDate(ref.getObjectId()));

            git.checkout().setName("master").call();
            git.branchDelete().setBranchNames(commitId).call();

        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    private void cloneRepository() throws GitAPIException {
        if(localFolder.exists()){
            try {
                FileUtils.deleteDirectory(localFolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        git = Git.cloneRepository()
                .setURI(url)
                .setCredentialsProvider( new UsernamePasswordCredentialsProvider(username, password))
                .setBranch(branch)
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
                for (RevCommit revCommit : git.log().add(git.getRepository().resolve(branch)).call()) {
                    Instant instant = Instant.ofEpochSecond(revCommit.getCommitTime());
                    LocalDateTime commitDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                    commitMap.put(revCommit.getName(),commitDate);
                }
            } catch (GitAPIException e) {
                e.printStackTrace();
            } catch (IncorrectObjectTypeException e) {
                e.printStackTrace();
            } catch (AmbiguousObjectException e) {
                e.printStackTrace();
            } catch (MissingObjectException e) {
                e.printStackTrace();
            } catch (IOException e) {
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

            Instant instant = Instant.ofEpochSecond(revCommit.getCommitTime());
            commitDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        } catch (IOException|GitAPIException e) {
            e.printStackTrace();
        }

        return commitDate;
    }
}
