package lu.uni.serval.analytics;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lu.uni.serval.utils.CommandRunner;
import lu.uni.serval.utils.Configuration;
import lu.uni.serval.utils.Plugin;
import org.apache.log4j.Logger;

import java.io.*;

public class ProjectAnalyticsCli implements CommandRunner {
    final static Logger logger = Logger.getLogger(ProjectAnalyticsCli.class);

    @Override
    public void run() throws Exception {
        Configuration config = Configuration.getInstance();
        Plugin analytics = config.getPlugin("report analytics");

        String gitUrl = (String)analytics.getAdditionalProperty("git url", "");
        String branch = (String)analytics.getAdditionalProperty("git branch", "master");
        String username = (String)analytics.getAdditionalProperty("git user", "");
        String password = (String)analytics.getAdditionalProperty("git password", "");

        ProjectAnalyzer projects = ProjectAnalyzer.fromGit(gitUrl, branch, username, password);
        EvolutionResults results = projects.findDifferences();

        exportReportEvolution(analytics, results);
        exportKeywordEvolution(analytics, results);
        exportKeywordNames(analytics, results);
        exportKeywordChangeSequence(analytics, results);
    }

    private void exportReportEvolution(Plugin analytics, EvolutionResults results) {
        if(analytics.getAdditionalProperty("output report differences file", "").equals("")){
            logger.warn("no output  report differences file provided");
            return;
        }

        String fileName = (String)analytics.getAdditionalProperty("output report differences file", "./report-differences.json");
        export(new ProjectEvolutionSerializer(), fileName, results);
    }

    private void exportKeywordEvolution(Plugin analytics, EvolutionResults results){
        if(analytics.getAdditionalProperty("output keyword differences file", "").equals("")){
            logger.warn("no output keyword differences file provided");
            return;
        }

        String fileName = (String)analytics.getAdditionalProperty("output keyword differences file", "./keyword-differences.json");
        export(new KeywordsEvolutionSerializer(), fileName, results);
    }

    private void exportKeywordNames(Plugin analytics, EvolutionResults results){
        if(analytics.getAdditionalProperty("output keyword names file", "").equals("")){
            logger.warn("no output keyword names file provided");
            return;
        }

        String fileName = (String)analytics.getAdditionalProperty("output keyword names file", "./keyword-names.json");
        export(new KeywordsNamesSerializer(), fileName, results);
    }

    private void exportKeywordChangeSequence(Plugin analytics, EvolutionResults results) {
        if(analytics.getAdditionalProperty("output keyword changes sequences file", "").equals("")){
            logger.warn("no output keyword changes sequences file provided");
            return;
        }

        String fileName = (String)analytics.getAdditionalProperty("output keyword changes sequences file", "./keyword-changes-sequences.json");
        export(new KeywordsChangeSequenceSerializer(), fileName, results);
    }

    private void export(JsonSerializer serializer, String fileName, EvolutionResults results){
        try {
            ObjectMapper mapper = new ObjectMapper();

            SimpleModule module = new SimpleModule();
            module.addSerializer(EvolutionResults.class, serializer);
            mapper.registerModule(module);

            File file = new File(fileName);
            mapper.writeValue(file, results);

            logger.info("results written to " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
