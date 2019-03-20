package org.ukwikora.analytics;

import java.util.*;

import org.ukwikora.model.*;
import org.ukwikora.utils.LevenshteinDistance;

public class CloneDetection<T extends Statement> {
    private static Set<Action.Type> ignoreForTypeI;
    private static Set<Action.Type> ignoreForTypeII;
    private static Set<Action.Type> ignoreForTypeIIExtended;

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

        Set<Action.Type> typeIIExtended = new HashSet<>(ignoreForTypeII);
        typeIIExtended.add(Action.Type.CHANGE_STEP);
        ignoreForTypeIIExtended = Collections.unmodifiableSet(typeIIExtended);
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

    public static <T extends Statement> Clones<T> findClones(Set<Project> projects, Class<T> type){
        CloneDetection<T> detection = new CloneDetection<>(projects);
        return detection.run(type);
    }

    public static <T extends Statement> Clones<T> findClones(Project project, Class<T> type){
        CloneDetection<T> detection = new CloneDetection<>(project);
        return detection.run(type);
    }

    public static  <T extends Statement> Clone.Type getCloneType(T t1, T t2){
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

        if(clone == Clone.Type.None && isCloneTypeIV(t1.getClass(), difference)){
            clone = Clone.Type.TypeIV;
        }

        return clone;
    }

    private Clones<T> run(Class<T> type){
        List<T> statements = new ArrayList<>();

        for(Project project: projects){
            statements.addAll(project.getStatements(type));
        }

        int size = statements.size();

        for(int i = 0; i < size; ++i){
            T t1 = statements.get(i);

            for(int j = i + 1; j < size; ++j){
                T t2 = statements.get(j);
                clones.update(t1, t2, getCloneType(t1, t2));
            }
        }

        return clones;
    }

    private static <T extends Statement> boolean isTooShort(T t1, T t2){
        if(KeywordDefinition.class.isAssignableFrom(t1.getClass())){
            KeywordDefinition keyword1 = (KeywordDefinition)t1;
            KeywordDefinition keyword2 = (KeywordDefinition)t2;

            return keyword1.getSteps().size() <= 1 || keyword2.getSteps().size() <= 1;
        }

        return false;
    }

    private static <T extends Statement> boolean isSameSize(T t1, T t2){
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

        if(!isTypeII && KeywordDefinition.class.isAssignableFrom(type)){
            isTypeII = isStepsClones(difference);
        }

        return isTypeII;
    }

    public static boolean isCloneTypeIV(Class<?> type, Difference difference){
        boolean isTypeIV = false;

        if(KeywordDefinition.class.isAssignableFrom(type)){
            isTypeIV = isSameSequence(difference);
        }

        return isTypeIV;
    }

    private static boolean isStepsClones(Difference difference){
        if(!difference.isEmpty(ignoreForTypeIIExtended)){
            return false;
        }

        for(Action action: difference.getActions()){
            if(action.getType() != Action.Type.CHANGE_STEP){
                continue;
            }

            Keyword left = getKeywordFromStep((Step)action.getLeft());
            Keyword right = getKeywordFromStep((Step)action.getRight());

            if(left == null || right == null){
                return false;
            }

            Difference stepDifference = Difference.of(left, right);

            if(!isCloneTypeI(Keyword.class, stepDifference)){
                return false;
            }
        }

        return true;
    }

    private static boolean isSameSequence(Difference difference) {
        KeywordDefinition left = (KeywordDefinition) difference.getLeft();
        KeywordDefinition right = (KeywordDefinition) difference.getRight();

        return LevenshteinDistance.index(left.getSteps(), right.getSteps()) == 0.0;
    }

    private static Keyword getKeywordFromStep(Step step){
        return step.getKeywordCall().map(KeywordCall::getKeyword).orElse(null);
    }
}
