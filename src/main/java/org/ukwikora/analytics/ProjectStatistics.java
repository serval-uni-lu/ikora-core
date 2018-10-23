package org.ukwikora.analytics;

import com.beust.jcommander.ParameterException;
import org.ukwikora.model.*;
import org.ukwikora.utils.LevenshteinDistance;
import org.ukwikora.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProjectStatistics {
    enum Metric{
        Size, Connectivity, Sequence, Depth, BranchIndex
    }

    final private Project project;

    private ElementTable<UserKeyword> userKeywords;
    private ElementTable<TestCase> testCases;

    public ProjectStatistics(Project project){
        this.project = project;
        userKeywords = this.project.getElements(UserKeyword.class);
        testCases = this.project.getElements(TestCase.class);
    }

    public int getNumberFiles(){
        return this.project.getTestCaseFiles().size();
    }

    public int getLoc(){
        return this.project.getLoc();
    }

    public <T extends KeywordDefinition> int getNumberKeywords(Class<T> type){
        ElementTable keywords = getKeywords(type);

        if(keywords != null){
            return keywords.size();
        }

        return 0;
    }

    public int getDocumentationLength() {
        int length = 0;

        Set<KeywordDefinition> keywords = new HashSet<>();
        keywords.addAll(userKeywords.toSet());
        keywords.addAll(testCases.toSet());

        for (KeywordDefinition keyword: keywords){
            length += StringUtils.countLines(keyword.getDocumentation());
        }

        return length;
    }

    public <T extends KeywordDefinition> int[] getSizeDistribution(Class<T> type){
        return getDistribution(type, Metric.Size);
    }

    public <T extends KeywordDefinition> int[] getConnectivityDistribution(Class<T> type){
        return getDistribution(type, Metric.Connectivity);
    }

    public <T extends KeywordDefinition> int[] getSequenceDistribution(Class<T> type){
        return getDistribution(type, Metric.Sequence);
    }

    public <T extends KeywordDefinition> int[] getDepthDistribution(Class<T> type){
        return getDistribution(type, Metric.Depth);
    }

    <T extends KeywordDefinition> int[] getBranchIndex(Class<T> type){
        return getDistribution(type, Metric.BranchIndex);
    }

    private <T extends KeywordDefinition> int[] getDistribution(Class<T> type, Metric metric){
        int[] distribution = new int[getNumberKeywords(type)];

        int index = 0;
        for(KeywordDefinition keyword: getKeywords(type)){
            int value = -1;

            switch (metric){
                case Size: value = keyword.getSize(); break;
                case Connectivity: value = keyword.getConnectivity(-1); break;
                case Sequence: value = keyword.getMaxSequenceSize(); break;
                case Depth: value = keyword.getDepth(); break;
                case BranchIndex: value = keyword.getBranchIndex(); break;
            }

            distribution[index++] = value;
        }

        return distribution;
    }

    private <T extends KeywordDefinition> ElementTable<T> getKeywords(Class<T> type){
        if(type == UserKeyword.class){
            return (ElementTable<T>) userKeywords;
        }
        else if(type == TestCase.class){
            return (ElementTable<T>) testCases;
        }

        throw new ParameterException("Unhandled type " + type.getName());
    }

    public static class DifferentiableString implements StatusResults.Differentiable {
        private String text;

        public DifferentiableString(String text){
            this.text = text;
        }

        @Override
        public double distance(StatusResults.Differentiable other) {
            if(!(other instanceof DifferentiableString)){
                return 1;
            }

            DifferentiableString otherDifferentiableString = (DifferentiableString)other;

            return LevenshteinDistance.stringIndex(this.text, otherDifferentiableString.text);
        }

        @Override
        public List<Action> differences(StatusResults.Differentiable other) {
            List<Action> actions = new ArrayList<>();

            if(other instanceof DifferentiableString && ((DifferentiableString)other).text.equals(this.text)){
                return actions;
            }

            actions.add(Action.changeName(this, other));

            return actions;
        }

        @Override
        public String getName() {
            return this.text;
        }
    }
}
