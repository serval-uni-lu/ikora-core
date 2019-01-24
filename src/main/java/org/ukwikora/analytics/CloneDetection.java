package org.ukwikora.analytics;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ukwikora.model.*;
import org.ukwikora.utils.LevenshteinDistance;

class CloneDetection<T extends Element> {
    private final static Logger logger = LogManager.getLogger(CloneDetection.class);

    static private Set<Action.Type> ignoreForTypeI;
    static private Set<Action.Type> ignoreForTypeII;
    static private Set<Action.Type> ignoreForTypeIIExtended;

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

    private Project project;
    private Clones<T> clones;

    private CloneDetection(Project project){
        this.project = project;
        this.clones = new Clones<>();
    }

    public static <T extends  Element> Clones<T> findClones(Project project, Class<T> type){
        CloneDetection<T> detection = new CloneDetection<>(project);
        return detection.run(type);
    }

    public static  <T extends  Element> Clones.Type getCloneType(T t1, T t2){
        Clones.Type clone = Clones.Type.None;

        if(isTooShort(t1, t2)){
            return clone;
        }

        Difference difference = Difference.of(t1, t2);

        if(isSameSize(t1, t2)){
            if(isCloneTypeI(t1.getClass(), difference)){
                clone = Clones.Type.TypeI;
            }
            else if(isCloneTypeII(t1.getClass(), difference)){
                clone = Clones.Type.TypeII;
            }
        }

        if(clone == Clones.Type.None && isCloneTypeIV(t1.getClass(), difference)){
            clone = Clones.Type.TypeIV;
        }

        return clone;
    }

    private Clones<T> run(Class<T> type){
        List<T> elements = project.getElements(type).asList();
        int size = elements.size();

        for(int i = 0; i < size; ++i){
            T t1 = elements.get(i);

            for(int j = i + 1; j < size; ++j){
                T t2 = elements.get(j);
                clones.update(t1, t2, getCloneType(t1, t2));
            }
        }

        return clones;
    }

    private static <T extends  Element> boolean isTooShort(T t1, T t2){
        if(KeywordDefinition.class.isAssignableFrom(t1.getClass())){
            KeywordDefinition keyword1 = (KeywordDefinition)t1;
            KeywordDefinition keyword2 = (KeywordDefinition)t2;

            return keyword1.getSteps().size() <= 1 || keyword2.getSteps().size() <= 1;
        }

        return false;
    }

    private static <T extends  Element> boolean isSameSize(T t1, T t2){
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
        if(step instanceof ForLoop){
            return null;
        }
        else if(step instanceof KeywordCall){
            return ((KeywordCall)step).getKeyword();
        }
        else if(step instanceof Assignment){
            KeywordCall call = ((Assignment)step).getExpression();
            if(call == null){
                return null;
            }

            return call.getKeyword();
        }

        return null;
    }
}
