package org.ukwikora.analytics;

import java.util.*;

public class DifferentiableMatcher {
    private Map<StatusResults.Differentiable, Set<StatusResults.Differentiable>> matched;
    private List<? extends StatusResults.Differentiable> differentiableList;
    private double threshold;

    private DifferentiableMatcher(List<? extends StatusResults.Differentiable> differentiableList, double threshold){
        this.differentiableList = differentiableList;
        this.matched = new HashMap<>();
        this.threshold = threshold;
    }

    public static DifferentiableMatcher match(List<? extends StatusResults.Differentiable> differentiableList, double threshold){
        DifferentiableMatcher matcher = new DifferentiableMatcher(differentiableList, threshold);

        for(StatusResults.Differentiable differentiable1: matcher.differentiableList){
            for(StatusResults.Differentiable differentiable2: matcher.differentiableList){
                if(differentiable1 == differentiable2){
                    continue;
                }

                matcher.compare(differentiable1, differentiable2);
            }
        }

        return matcher;
    }

    private void compare(StatusResults.Differentiable differentiable1, StatusResults.Differentiable differentiable2){
        Set<StatusResults.Differentiable> set = this.matched.getOrDefault(differentiable1, new HashSet<>());

        if((1 - differentiable1.distance(differentiable2)) >= threshold){
            set.add(differentiable2);
        }

        this.matched.put(differentiable1, set);
    }

    public Map<StatusResults.Differentiable, Set<StatusResults.Differentiable>> getMatched(){
        return this.matched;
    }
}
