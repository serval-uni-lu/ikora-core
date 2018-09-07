package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.Keyword;
import lu.uni.serval.robotframework.model.KeywordTable;
import lu.uni.serval.robotframework.model.Project;
import lu.uni.serval.robotframework.model.UserKeyword;

public class ProjectStatistics {
    enum Metric{
        Size, Complexity, Sequence
    }

    final private Project project;

    private KeywordTable keywords;


    ProjectStatistics(Project project){
        this.project = project;
        keywords = null;
    }

    public int getNumberFiles(){
        return this.project.getTestCaseFiles().size();
    }

    public int getNumberKeywords(){
        return getKeywords().size();
    }

    public int[] getSizeDistribution(){
        return getDistribution(Metric.Size);
    }

    public int[] getComplexityDistribution(){
        return getDistribution(Metric.Complexity);
    }

    public int[] getSequenceDistribution(){
        return getDistribution(Metric.Sequence);
    }

    public int[] getDistribution(Metric metric){
        int[] distribution = new int[getNumberKeywords()];

        int index = 0;
        for(Keyword keyword: getKeywords()){
            int value = -1;

            switch (metric){
                case Size: value = keyword.getSize(); break;
                case Complexity: value = keyword.getDependencies().size(); break;
                case Sequence: value = keyword.getSequence().size(); break;
            }

            distribution[index++] = value;
        }

        return distribution;
    }

    private KeywordTable<UserKeyword> getKeywords(){
        if(keywords == null){
            keywords = this.project.getUserKeywords();
        }

        return keywords;
    }
}
