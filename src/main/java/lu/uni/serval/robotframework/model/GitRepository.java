package lu.uni.serval.robotframework.model;

import lu.uni.serval.robotframework.compiler.Compiler;
import lu.uni.serval.utils.Configuration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
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
import java.util.*;

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
        GitCommit commit = getMostRecentCommit(dateTime);
        checkout(commit.getId());
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

            git.checkout().setName(branch).call();
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

    public GitCommit getMostRecentCommit(LocalDateTime dateTime){
        GitCommit mostRecentCommit = null;

        try {
            if(git == null){
                cloneRepository();
            }

            List<GitCommit> commits =  getRevisions();

            for (GitCommit commit: commits) {
                if(commit.getDateTime().isAfter(dateTime)){
                    break;
                }

                mostRecentCommit = commit;
            }

            return mostRecentCommit;
        }
        catch (GitAPIException e){
            mostRecentCommit = null;
            e.printStackTrace();
        }

        return mostRecentCommit;
    }

    public List<GitCommit> getRevisions() {
        List<GitCommit> commits = new ArrayList<>();

        try {
            if(git == null){
                cloneRepository();
            }

            Iterable<RevCommit> revCommits = null;

            if(branch.equals("master")){
                revCommits = git.log().call();
            }
            else{
                ObjectId masterId = git.getRepository().resolve("remotes/origin/master");
                ObjectId branchId = git.getRepository().resolve("remotes/origin/" + branch);

                revCommits = git.log().addRange(masterId, branchId).call();
            }

            for (RevCommit revCommit : revCommits) {
                Instant instant = Instant.ofEpochSecond(revCommit.getCommitTime());
                LocalDateTime commitDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                commits.add(new GitCommit(revCommit.getName(), commitDate));
            }

            commits.sort(Comparator.comparing(GitCommit::getDateTime));

            return commits;
        }
        catch (GitAPIException | IOException e) {
            logger.error("Failed to get logs for branch " + branch);

            return new ArrayList<>();
        }
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
