package lu.uni.serval.analytics;

import com.fasterxml.jackson.databind.ObjectMapper;
import lu.uni.serval.robotframework.model.Project;
import lu.uni.serval.robotframework.parser.ProjectParser;
import lu.uni.serval.utils.CommandRunner;
import lu.uni.serval.utils.Configuration;
import lu.uni.serval.utils.Plugin;
import lu.uni.serval.utils.exception.DuplicateNodeException;

import java.io.File;
import java.io.IOException;

public class KeywordsAnalyticsCli implements CommandRunner{

    @Override
    public void run() throws DuplicateNodeException {

        Configuration config = Configuration.getInstance();
        Plugin analytics = config.getPlugin("keywords analytics");

        CloneIndex.setKeywordThreshold((double)analytics.getAdditionalProperty("keyword threshold", 0.9));
        CloneIndex.setTreeThreshold((double)analytics.getAdditionalProperty("tree threshold", 0.9));

        String location = (String)analytics.getAdditionalProperty("testCase location", "");

        Project project = ProjectParser.parse(location);

        if(project == null){
            return;
        }

        CloneDetection cloneDetection = new CloneDetection();
        //final CloneResults cloneResults = cloneDetection.findClones(forest);

        Statistics statistics = new Statistics();
        //final StatisticsResults statisticsResults = statistics.computeStatistics(forest);

        try {
            ObjectMapper mapper = new ObjectMapper();
            KeywordsAnalysisJsonExport export = new KeywordsAnalysisJsonExport();
            //export.setClones(cloneResults);
            //export.setGeneralStatistics(statisticsResults);

            File file = new File((String)analytics.getAdditionalProperty("output file", "./analytics.json"));

            mapper.writeValue(file, export);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
