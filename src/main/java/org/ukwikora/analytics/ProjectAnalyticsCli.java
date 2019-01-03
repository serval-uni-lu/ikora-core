package org.ukwikora.analytics;

import org.apache.commons.io.FileUtils;
import org.ukwikora.compiler.Compiler;
import org.ukwikora.model.Project;
import org.ukwikora.model.UserKeyword;
import org.ukwikora.utils.CommandRunner;
import org.ukwikora.utils.Configuration;
import org.ukwikora.utils.Plugin;
import org.gitlabloader.api.Gitlab;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProjectAnalyticsCli implements CommandRunner {
    @Override
    public void run() throws Exception {
        Configuration config = Configuration.getInstance();
        Plugin analytics = config.getPlugin("project analytics");
        String location = loadProjects(analytics);

        Project project = Compiler.compile(location);
        CloneDetection.findClones(project, UserKeyword.class);

    }

    private String loadProjects(Plugin analytics) throws IOException {
        String url = analytics.getPropertyAsString("git url", "");
        String username = analytics.getPropertyAsString("git user", "");
        String password = analytics.getPropertyAsString("git password", "");
        String token = analytics.getPropertyAsString("git token", "");
        String group = analytics.getPropertyAsString("git group");
        String location = analytics.getPropertyAsString("project location");
        String defaultBranch = analytics.getPropertyAsString("git default branch", "master");
        Map<String, String> branchExceptions = analytics.getPropertyAsStringMap("git branch exception");

        location = createTmpFolder(location);

        Gitlab gitlab = new Gitlab().setCredentials(username, password).setToken(token).setUrl(url);
        List<org.gitlabloader.api.Project> projects = gitlab.findProjectsByGroupName(group);
        gitlab.cloneProjects(projects, location, defaultBranch, branchExceptions);

        return location;
    }

    private String createTmpFolder(String location) throws IOException {
        File folder = new File(location);

        if(location.isEmpty() || !folder.isDirectory()){
            File tmp = FileUtils.getTempDirectory();
            Date date = new Date();
            Timestamp ts = new Timestamp(date.getTime());
            String folderName = "project-analytics-" + ts.toInstant().toEpochMilli();
            folder = new File(tmp, folderName);

            location = folder.getAbsolutePath();
        }

        if(!folder.exists()) {
            folder.mkdir();
        }

        return location;
    }
}
