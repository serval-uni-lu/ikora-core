package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.KeywordDefinition;
import lu.uni.serval.robotframework.model.Step;
import lu.uni.serval.utils.nlp.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.math.NumberUtils.min;

public class Difference {

    private KeywordDefinition before;
    private KeywordDefinition after;
    private List<Action> actions;

    private Difference(KeywordDefinition before, KeywordDefinition after){
        this.before = before;
        this.after = after;

        this.actions = new ArrayList<>();
    }

    public KeywordDefinition getBefore(){
        return before;
    }

    public KeywordDefinition getAfter(){
        return after;
    }

    public static Difference of(KeywordDefinition before, KeywordDefinition after) {
        Difference difference = new Difference(before, after);

        if(before == after){
            return difference;
        }

        if(before.getName() != after.getName()){

        }

        difference.extractStepDifferences();

        return difference;
    }

    private void extractStepDifferences(){
        double[][] distances = computeDistanceMatrix(before.getSteps(), after.getSteps());
        computeEditMapping(distances);
    }

    private double[][] computeDistanceMatrix(List<Step> before, List<Step> after) {
        double[][] d = new double[before.size() + 1][after.size() + 1];

        for(int i = 0; i <= before.size(); ++i){
            for(int j = 0; j <= after.size(); ++j){
                if(i == 0){
                    d[i][j] = j;
                }
                else if (j == 0){
                    d[i][j] = i;
                }
                else {
                    d[i][j] = min(d[i - 1][j - 1]
                                    + costOfSubstitution(before.get(i - 1), after.get(j - 1)),
                            d[i - 1][j] + 1,
                            d[i][j - 1] + 1);
                }
            }
        }

        return d;
    }

    private double costOfSubstitution(Step before, Step after){
        return StringUtils.levenshteinIndex(before.getName().toString(), after.getName().toString());
    }

    private void computeEditMapping(double[][] distances) {
        int xPosition = before.getSteps().size();
        int yPosition = after.getSteps().size();

        double value = distances[xPosition][yPosition];

        while(value != 0){
            double substitution = distances[xPosition - 1][yPosition - 1];
            double subtraction = distances[xPosition][yPosition - 1];
            double addition = distances[xPosition - 1][yPosition];

            if(substitution < subtraction && substitution < addition){
                actions.add(Action.changeName(xPosition - 1, yPosition - 1));

                value = substitution;
                xPosition -= 1;
                yPosition -= 1;
            }
            else if (subtraction < addition){
                actions.add(Action.removeStep(yPosition - 1));

                value = subtraction;
                yPosition -= 1;
            }
            else{
                actions.add(Action.insertStep(xPosition - 1));

                value = addition;
                xPosition -= 1;
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        if(this == other){
            return true;
        }

        if(other == null){
            return false;
        }

        if(this.getClass() != other.getClass()){
            return false;
        }

        Difference difference = (Difference)other;

        return this.before == difference.before
                && this.after == difference.after;
    }

    @Override
    public int hashCode(){
        int hash = 7;
        hash = getNodeHash(hash, before);
        hash = getNodeHash(hash, after);

        return hash;
    }

    private int getNodeHash(int hash, KeywordDefinition keyword){
        if(keyword == null){
            hash = 31 * hash;
        }
        else {
            hash = 31 * hash + (keyword.getFile() == null ? 0 : keyword.getFile().hashCode());
            hash = 31 * hash + (keyword.getName().toString() == null ? 0 : keyword.getName().toString().hashCode());
        }

        return hash;
    }
}
