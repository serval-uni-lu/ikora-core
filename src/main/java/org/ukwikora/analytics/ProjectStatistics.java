package org.ukwikora.analytics;

import com.beust.jcommander.ParameterException;
import org.ukwikora.model.*;
import org.ukwikora.utils.StringUtils;

import java.util.*;

public class ProjectStatistics {
    enum Metric{
        Size, Connectivity, Sequence, Level, BranchIndex
    }

    final private Project project;

    private Set<UserKeyword> userKeywords;
    private Set<TestCase> testCases;

    public ProjectStatistics(Project project){
        this.project = project;
        userKeywords = this.project.getStatements(UserKeyword.class);
        testCases = this.project.getStatements(TestCase.class);
    }

    public int getNumberFiles(){
        return this.project.getTestCaseFiles().size();
    }

    public int getLoc(){
        return this.project.getLoc();
    }

    public <T extends KeywordDefinition> int getNumberKeywords(Class<T> type){
        Set<T> keywords = getStatements(type);

        if(keywords != null){
            return keywords.size();
        }

        return 0;
    }

    public int getDocumentationLength() {
        int length = 0;

        Set<Statement> keywords = new HashSet<>();
        keywords.addAll(userKeywords);
        keywords.addAll(testCases);

        for (Statement keyword: keywords){
            length += StringUtils.countLines(((KeywordDefinition)keyword).getDocumentation());
        }

        return length;
    }

    public <T extends Statement> Map<Integer, Integer> getSizeDistribution(Class<T> type){
        return getDistribution(type, Metric.Size);
    }

    public <T extends Statement> Map<Integer, Integer> getConnectivityDistribution(Class<T> type){
        return getDistribution(type, Metric.Connectivity);
    }

    public <T extends Statement> Map<Integer, Integer> getSequenceDistribution(Class<T> type){
        return getDistribution(type, Metric.Sequence);
    }

    public <T extends Statement> Map<Integer, Integer> getLevelDistribution(Class<T> type){
        return getDistribution(type, Metric.Level);
    }

    public <T extends Statement> Map<String, Integer> getDeadCodeDistriburation(Class<T> type){
        Map<String, Integer> deadCode = new HashMap<>(2);

        for(Map.Entry<Integer, Integer> entry: getConnectivityDistribution(type).entrySet()){
            if(entry.getKey() == 0){
                deadCode.merge("Dead", entry.getValue(), Integer::sum);
            }
            else{
                deadCode.merge("Used", entry.getValue(), Integer::sum);
            }
        }

        return deadCode;
    }

    <T extends Statement> Map<Integer, Integer> getBranchIndex(Class<T> type){
        return getDistribution(type, Metric.BranchIndex);
    }

    private <T extends Statement> Map<Integer, Integer> getDistribution(Class<T> type, Metric metric){
        Map<Integer, Integer> distribution = new TreeMap<>();

        for(Statement statement: getStatements(type)){
            int value = -1;

            switch (metric){
                case Size: value = KeywordStatistics.getSize(statement); break;
                case Connectivity: value = KeywordStatistics.getConnectivity(statement); break;
                case Sequence: value = KeywordStatistics.getSequenceSize(statement); break;
                case Level: value = KeywordStatistics.getLevel(statement); break;
                //case BranchIndex: value = keyword.getBranchIndex(); break;
            }

            distribution.merge(value, 1, Integer::sum);
        }

        return distribution;
    }

    private <T extends Statement> Set<T> getStatements(Class<T> type){
        if(type == UserKeyword.class){
            return (Set<T>)userKeywords;
        }
        else if(type == TestCase.class){
            return (Set<T>)testCases;
        }

        throw new ParameterException("Unhandled type " + type.getName());
    }
}
