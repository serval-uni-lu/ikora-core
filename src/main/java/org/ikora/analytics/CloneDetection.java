package org.ikora.analytics;

import java.util.*;

import org.ikora.model.*;
import org.ikora.utils.LevenshteinDistance;

import javax.swing.text.html.Option;

public class CloneDetection<T extends Node> {
    private static Set<Action.Type> ignoreForTypeI;
    private static Set<Action.Type> ignoreForTypeII;

    static {
        Set<Action.Type> typeI = new HashSet<>(4);
        typeI.add(Action.Type.CHANGE_NAME);
        typeI.add(Action.Type.CHANGE_DOCUMENTATION);
        typeI.add(Action.Type.REMOVE_DOCUMENTATION);
        typeI.add(Action.Type.ADD_DOCUMENTATION);
        ignoreForTypeI = Collections.unmodifiableSet(typeI);

        Set<Action.Type> typeII = new HashSet<>(ignoreForTypeI);
        typeII.add(Action.Type.CHANGE_STEP_ARGUMENTS);
        typeII.add(Action.Type.CHANGE_STEP_RETURN_VALUES);
        ignoreForTypeII = Collections.unmodifiableSet(typeII);
    }

    private Set<Project> projects;
    private Clones<T> clones;

    private CloneDetection(Project project){
        this.projects = Collections.singleton(project);
        this.clones = new Clones<>();
    }

    private CloneDetection(Set<Project> projects){
        this.projects = projects;
        this.clones = new Clones<>();
    }

    public static <T extends Node> Clones<T> findClones(Set<Project> projects, Class<T> type){
        CloneDetection<T> detection = new CloneDetection<>(projects);
        return detection.run(type);
    }

    public static <T extends Node> Clones<T> findClones(Project project, Class<T> type){
        CloneDetection<T> detection = new CloneDetection<>(project);
        return detection.run(type);
    }

    public static  <T extends Node> Clone.Type getCloneType(T t1, T t2){
        Clone.Type clone = Clone.Type.None;

        if(isTooShort(t1, t2)){
            return clone;
        }

        Difference difference = Difference.of(t1, t2);

        if(isSameSize(t1, t2)){
            if(isCloneTypeI(t1.getClass(), difference)){
                clone = Clone.Type.TypeI;
            }
            else if(isCloneTypeII(t1.getClass(), difference)){
                clone = Clone.Type.TypeII;
            }
        }

        if(clone == Clone.Type.None){
            if(isCloneTypeIII(t1.getClass(), difference)){
                clone = Clone.Type.TypeIII;
            }
            else if (isCloneTypeIV(t1.getClass(), difference)){
                clone = Clone.Type.TypeIV;
            }
        }

        return clone;
    }

    private Clones<T> run(Class<T> type){
        List<T> nodes = new ArrayList<>();

        for(Project project: projects){
            nodes.addAll(project.getNodes(type));
        }

        int size = nodes.size();

        for(int i = 0; i < size; ++i){
            T t1 = nodes.get(i);

            for(int j = i + 1; j < size; ++j){
                T t2 = nodes.get(j);
                clones.update(t1, t2, getCloneType(t1, t2));
            }
        }

        return clones;
    }

    private static <T extends Node> boolean isTooShort(T t1, T t2){
        if(KeywordDefinition.class.isAssignableFrom(t1.getClass())){
            KeywordDefinition keyword1 = (KeywordDefinition)t1;
            KeywordDefinition keyword2 = (KeywordDefinition)t2;

            return keyword1.getSteps().size() <= 1 || keyword2.getSteps().size() <= 1;
        }

        return false;
    }

    private static <T extends Node> boolean isSameSize(T t1, T t2){
        if(!KeywordDefinition.class.isAssignableFrom(t1.getClass())){
            return true;
        }

        return ((KeywordDefinition)t1).getSteps().size() == ((KeywordDefinition)t2).getSteps().size();
    }

    public static boolean isCloneTypeI(Class<?> type, Difference difference){
        return difference.isEmpty(ignoreForTypeI);
    }

    public static boolean isCloneTypeII(Class<?> type, Difference difference){
        boolean isTypeII = difference.isEmpty(ignoreForTypeII);

        //if(!isTypeII && KeywordDefinition.class.isAssignableFrom(type)){
        //    isTypeII = isStepsClones(difference);
        //}

        return isTypeII;
    }

    public static boolean isCloneTypeIII(Class<?> type, Difference difference){
        for(Action action: difference.getActions()){
            if (!ignoreForTypeII.contains(action.getType())
            && !canIgnoreForTypeIII(action)){
                return false;
            }
        }

        return true;
    }

    private static boolean canIgnoreForTypeIII(Action action) {

        switch (action.getType()){
            case CHANGE_STEP_ARGUMENTS:
            case CHANGE_STEP: return isLogStep(action.getLeft()) && isLogStep(action.getRight());
            case ADD_STEP:
            case REMOVE_STEP: return isLogStep(action.getValue());
            default: return false;
        }
    }

    private static boolean isLogStep(Differentiable differentiable){
        if(differentiable == null){
            return false;
        }

        if(!KeywordCall.class.isAssignableFrom(differentiable.getClass())){
            return false;
        }

        Optional<Keyword> optionalKeyword = ((KeywordCall)differentiable).getKeyword();

        return optionalKeyword.filter(keyword -> keyword.getType() == Keyword.Type.Log).isPresent();
    }

    public static boolean isCloneTypeIV(Class<?> type, Difference difference){
        boolean isTypeIV = false;

        if(KeywordDefinition.class.isAssignableFrom(type)){
            isTypeIV = isSameSequence(difference);
        }

        return isTypeIV;
    }
/*
    private static boolean isStepsClones(Difference difference){
        if(!difference.isEmpty(ignoreForTypeIIExtended)){
            return false;
        }

        for(Action action: difference.getActions()){
            if(action.getType() != Action.Type.CHANGE_STEP){
                continue;
            }

            Optional<Keyword> left = getKeywordFromStep((Step)action.getLeft());
            Optional<Keyword> right = getKeywordFromStep((Step)action.getRight());

            if(!left.isPresent() || !right.isPresent()){
                return false;
            }

            Difference stepDifference = Difference.of(left.get(), right.get());

            if(!isCloneTypeI(Keyword.class, stepDifference)){
                return false;
            }
        }

        return true;
    }
*/
    private static boolean isSameSequence(Difference difference) {
        KeywordDefinition left = (KeywordDefinition) difference.getLeft();
        KeywordDefinition right = (KeywordDefinition) difference.getRight();

        return LevenshteinDistance.index(left.getSteps(), right.getSteps()) == 0.0;
    }

    private static Optional<Keyword> getKeywordFromStep(Step step){
        return  step.getKeywordCall().flatMap(KeywordCall::getKeyword);
    }
}
