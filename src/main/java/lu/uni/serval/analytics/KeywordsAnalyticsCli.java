package lu.uni.serval.analytics;

import com.fasterxml.jackson.databind.ObjectMapper;
import lu.uni.serval.robotframework.model.KeywordsParser;
import lu.uni.serval.utils.CommandRunner;
import lu.uni.serval.utils.Configuration;
import lu.uni.serval.utils.Plugin;
import lu.uni.serval.utils.exception.DuplicateNodeException;
import lu.uni.serval.utils.tree.TreeNode;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class KeywordsAnalyticsCli implements CommandRunner{

    @Override
    public void run() throws DuplicateNodeException {

        Configuration config = Configuration.getInstance();
        Plugin analytics = config.getPlugin("keyword analytics");

        CloneIndex.setKeywordThreshold((double)analytics.getAddictionalProperty("keyword threshold", 0.9));
        CloneIndex.setTreeThreshold((double)analytics.getAddictionalProperty("tree threshold", 0.9));

        String location = (String)analytics.getAddictionalProperty("testCase location", "");

        Set<TreeNode> forest = KeywordsParser.parse(location);

        if(forest == null){
            return;
        }

        CloneDetection cloneDetection = new CloneDetection();
        final CloneResults cloneResults = cloneDetection.findClones(forest);

        Statistics statistics = new Statistics();
        final StatisticsResults statisticsResults = statistics.computeStatistics(forest);

        ObjectMapper mapper = new ObjectMapper();
        try {
            KeywordsAnalysisJsonExport export = new KeywordsAnalysisJsonExport();
            export.setClones(cloneResults);
            export.setGeneralStatistics(statisticsResults);

            File file = new File((String)analytics.getAddictionalProperty("output file", "./analytics.json"));

            mapper.writeValue(file, export);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
