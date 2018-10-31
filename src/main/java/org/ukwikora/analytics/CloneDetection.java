package org.ukwikora.analytics;

import java.util.*;
import org.apache.log4j.Logger;
import org.ukwikora.model.*;

public class CloneDetection<T extends Element> {
    final static Logger logger = Logger.getLogger(CloneDetection.class);

    static private Set<Action.Type> ignoreForTypeI;
    static private Set<Action.Type> ignoreForTypeII;

    static {
        Set<Action.Type> typeI = new HashSet<>(4);
        typeI.add(Action.Type.CHANGE_NAME);
        typeI.add(Action.Type.CHANGE_DOCUMENTATION);
        typeI.add(Action.Type.REMOVE_DOCUMENTATION);
        typeI.add(Action.Type.ADD_DOCUMENTATION);
        ignoreForTypeI = Collections.unmodifiableSet(typeI);

        Set<Action.Type> typeII = new HashSet<>(6);
        typeII.add(Action.Type.CHANGE_NAME);
        typeII.add(Action.Type.CHANGE_DOCUMENTATION);
        typeII.add(Action.Type.REMOVE_DOCUMENTATION);
        typeII.add(Action.Type.ADD_DOCUMENTATION);
        typeII.add(Action.Type.CHANGE_STEP_ARGUMENTS);
        typeII.add(Action.Type.CHANGE_STEP_RETURN_VALUES);
        ignoreForTypeII = Collections.unmodifiableSet(typeII);
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

        if(isSameSize(t1, t2)){
            Difference difference = Difference.of(t1, t2);

            if(isCloneTypeI(t1.getClass(), difference)){
                clone = Clones.Type.TypeI;
            }
            else if(isCloneTypeII(t1.getClass(), difference)){
                clone = Clones.Type.TypeII;
            }
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

        if(!isTypeII && type == KeywordDefinition.class){
            isTypeII = isStepsClones(difference);
        }

        return isTypeII;
    }

    private static boolean isStepsClones(Difference difference){
        Set<Action.Type> types =  ignoreForTypeII;
        types.add(Action.Type.CHANGE_STEP);

        if(!difference.isEmpty(types)){
            return false;
        }

        for(Action action: difference.getActions()){
            if(action.getType() != Action.Type.CHANGE_STEP){
                continue;
            }

            Keyword left = getKeywordFromStep((Step)action.getLeft());
            Keyword right = getKeywordFromStep((Step)action.getLeft());

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
