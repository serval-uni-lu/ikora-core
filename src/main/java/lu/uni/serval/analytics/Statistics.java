package lu.uni.serval.analytics;

import lu.uni.serval.utils.tree.TreeNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistics {
    private enum Type{
        Size, Depth, Steps
    }

    private class StaticitcsElement {
        int maximum;
        int minimum;
        int sum;

        void reset(){
            maximum = 0;
            minimum = Integer.MAX_VALUE;
            sum = 0;
        }
    }

    private int numberOfKeywords;
    private Map<Type, StaticitcsElement> statistics;

    public Statistics(){
        statistics = new HashMap<Type, StaticitcsElement>();
        statistics.put(Type.Size, new StaticitcsElement());
        statistics.put(Type.Depth, new StaticitcsElement());
        statistics.put(Type.Steps, new StaticitcsElement());
    }

    public StatisticsResults computeStatistics(List<TreeNode> forest){
        StatisticsResults results = new StatisticsResults();

        reset();
        numberOfKeywords = forest.size();

        for(TreeNode tree: forest){
            updateStatistic(Type.Size, tree.getSize());
            updateStatistic(Type.Depth, tree.getDepth());
            updateStatistic(Type.Steps, tree.getLeaves().size());
        }

        results.setIntegerStatistics("NumberOfKeywords", numberOfKeywords);

        results.setIntegerStatistics("MaximumSize", statistics.get(Type.Size).maximum );
        results.setIntegerStatistics("MinimumSize", statistics.get(Type.Size).minimum );
        results.setDoubleStatistics("AverageSize", statistics.get(Type.Size).sum / numberOfKeywords);

        results.setIntegerStatistics("MaximumDepth", statistics.get(Type.Depth).maximum );
        results.setIntegerStatistics("MinimumDepth", statistics.get(Type.Depth).minimum );
        results.setDoubleStatistics("AverageDepth", statistics.get(Type.Depth).sum / numberOfKeywords);

        results.setIntegerStatistics("MaximumSteps", statistics.get(Type.Steps).maximum );
        results.setIntegerStatistics("MinimumSteps", statistics.get(Type.Steps).minimum );
        results.setDoubleStatistics("AverageSteps", statistics.get(Type.Steps).sum / numberOfKeywords);

        return results;
    }

    private void reset(){
        numberOfKeywords = 0;

        for(Map.Entry<Type, StaticitcsElement> statistic: statistics.entrySet()){
            statistic.getValue().reset();
        }
    }


    private void updateStatistic(Type type, int value){
        if(value > statistics.get(type).maximum){
            statistics.get(type).maximum = value;
        }

        if(value < statistics.get(type).minimum){
            statistics.get(type).minimum = value;
        }

        statistics.get(type).sum += value;
    }
}
