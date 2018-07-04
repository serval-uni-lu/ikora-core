package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.KeywordDefinition;
import lu.uni.serval.robotframework.model.Step;

import java.util.ArrayList;
import java.util.List;

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

        computeStepDifferences(difference, before.getSteps(), after.getSteps());

        return difference;
    }

    private static void computeStepDifferences(Difference difference, List<Step> before, List<Step> after) {
        int[][] d = new int[before.size() + 1][after.size() + 1];

        for(int i = 0; i < before.size(); ++i){
            for(int j = 0; j < after.size(); ++j){
                if(i == 0){
                    d[i][j] = j;
                }
                else if (j == 0){
                    d[i][j] = i;
                }
                else {

                    if(d[i - 1][j - 1] <= d[i - 1][j] + 1
                            && d[i - 1][j - 1] <= d[i][j - 1] + 1){
                        d[i][j] = d[i - 1][j - 1];
                    }
                    else if(d[i - 1][j] < d[i][j -1]){
                        d[i][j] = d[i - 1][j] + 1;
                    }
                    else{
                        d[i][j] = d[i][j - 1] + 1;
                    }
                }
            }
        }

        System.out.println(d);
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
