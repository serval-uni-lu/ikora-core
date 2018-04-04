package lu.uni.serval.analytics;

import java.util.HashMap;
import java.util.Map;

public class StatisticsResults {
    private Map<String, Integer> integerStatistics;
    private Map<String, Double> doubleStatistics;

    StatisticsResults(){
        integerStatistics = new HashMap<>();
        doubleStatistics = new HashMap<>();
    }

    public void setDoubleStatistics(String name, double value) {
        doubleStatistics.put(name, value);
    }

    public void setIntegerStatistics(String name, int value){
        integerStatistics.put(name, value);
    }

    public Map<String, Integer> getIntegerStatistics(){
        return integerStatistics;
    }

    public Map<String, Double> getDoubleStatistics(){
        return doubleStatistics;
    }
}
