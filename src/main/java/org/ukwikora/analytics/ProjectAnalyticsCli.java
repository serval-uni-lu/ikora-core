package org.ukwikora.analytics;

import org.ukwikora.compiler.Compiler;
import org.ukwikora.model.Project;
import org.ukwikora.utils.CommandRunner;
import org.ukwikora.utils.Configuration;
import org.ukwikora.utils.Plugin;

public class ProjectAnalyticsCli implements CommandRunner {
    @Override
    public void run() throws Exception {
        Configuration config = Configuration.getInstance();
        Plugin analytics = config.getPlugin("project analytics");

        String location = analytics.getPropertyAsString("location");
        Project project = Compiler.compile(location);
    }
}
