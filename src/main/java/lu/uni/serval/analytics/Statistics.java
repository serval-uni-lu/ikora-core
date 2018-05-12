package lu.uni.serval.analytics;

import lu.uni.serval.utils.tree.LabelTreeNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Statistics {
    private enum Type{
        Size, Depth, Steps
    }

    private class StatisticsElement {
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
    private Map<Type, StatisticsElement> statistics;

    public Statistics(){
        statistics = new HashMap<>();
        statistics.put(Type.Size, new StatisticsElement());
        statistics.put(Type.Depth, new StatisticsElement());
        statistics.put(Type.Steps, new StatisticsElement());
    }

    public StatisticsResults computeStatistics(Set<LabelTreeNode> forest){
        StatisticsResults results = new StatisticsResults();

        reset();
        numberOfKeywords = forest.size();

        for(LabelTreeNode tree: forest){
            updateStatistic(Type.Size, tree.getNodeCount());
            updateStatistic(Type.Depth, tree.getDepth());
            updateStatistic(Type.Steps, tree.getLeaves().size());
        }

        results.setIntegerStatistics("NumberOfKeywords", numberOfKeywords);

        results.setIntegerStatistics("MaximumSize", statistics.get(Type.Size).maximum );
        results.setIntegerStatistics("MinimumSize", statistics.get(Type.Size).minimum );

        results.setDoubleStatistics("AverageSize", divide(statistics.get(Type.Size).sum, numberOfKeywords));

        results.setIntegerStatistics("MaximumDepth", statistics.get(Type.Depth).maximum );
        results.setIntegerStatistics("MinimumDepth", statistics.get(Type.Depth).minimum );
        results.setDoubleStatistics("AverageDepth", divide(statistics.get(Type.Depth).sum, numberOfKeywords));

        results.setIntegerStatistics("MaximumSteps", statistics.get(Type.Steps).maximum );
        results.setIntegerStatistics("MinimumSteps", statistics.get(Type.Steps).minimum );
        results.setDoubleStatistics("AverageSteps", divide(statistics.get(Type.Steps).sum, numberOfKeywords));

        return results;
    }

    private void reset(){
        numberOfKeywords = 0;

        for(Map.Entry<Type, StatisticsElement> statistic: statistics.entrySet()){
            statistic.getValue().reset();
        }
    }

    private static double divide(double numerator, double denominator){
        double result;

        try{
            result = numerator / denominator;
        }
        catch(ArithmeticException e){
            result = 0.0;
        }

        return result;
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
