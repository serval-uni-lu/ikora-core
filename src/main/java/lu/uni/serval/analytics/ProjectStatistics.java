package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.*;

public class ProjectStatistics {
    enum Metric{
        Size, Complexity, Sequence
    }

    final private Project project;

    private ElementTable keywords;


    ProjectStatistics(Project project){
        this.project = project;
        keywords = null;
    }

    public int getNumberFiles(){
        return this.project.getTestCaseFiles().size();
    }

    public int getNumberKeywords(){
        return getKeywords(UserKeyword.class).size();
    }

    public <T extends KeywordDefinition> int[] getSizeDistribution(Class<T> type){
        return getDistribution(type, Metric.Size);
    }

    public <T extends KeywordDefinition> int[] getComplexityDistribution(Class<T> type){
        return getDistribution(type, Metric.Complexity);
    }

    public <T extends KeywordDefinition> int[] getSequenceDistribution(Class<T> type){
        return getDistribution(type, Metric.Sequence);
    }

    public <T extends KeywordDefinition> int[] getDistribution(Class<T> type, Metric metric){
        int[] distribution = new int[getNumberKeywords()];

        int index = 0;
        for(KeywordDefinition keyword: getKeywords(type)){
            int value = -1;

            switch (metric){
                case Size: value = keyword.getSize(); break;
                case Complexity: value = keyword.getDependencies().size(); break;
                case Sequence: value = keyword.getSequences().get(0).size(); break;
            }

            distribution[index++] = value;
        }

        return distribution;
    }

    private <T extends KeywordDefinition> ElementTable<T> getKeywords(Class<T> type){
        if(keywords == null){
            keywords = this.project.getElements(type);
        }

        return keywords;
    }
}
