package lu.uni.serval.analytics;

import lu.uni.serval.robotframework.model.*;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import org.apache.log4j.Logger;

public class CloneDetection<T extends  Element> {
    final static Logger logger = Logger.getLogger(CloneDetection.class);

    private Project project;

    private CloneResults<T> clones;

    private CloneDetection(Project project){
        this.project = project;
        this.clones = new CloneResults<>();
    }

    public static <T extends  Element> CloneResults<T> findClones(Project project, Class<T> type){
        CloneDetection<T> detection = new CloneDetection<>(project);
        return detection.run(type);
    }

    public static  <T extends  Element> CloneResults.Type getCloneType(T t1, T t2){
        CloneResults.Type type = CloneResults.Type.None;

        if(isSameSize(t1, t2) && !isTooShort(t1, t2)){
            Difference difference = Difference.of(t1, t2);

            if(isType1(t1.getClass(), difference)){
                type = CloneResults.Type.TypeI;
            }
            else if(isType2(t1.getClass(), difference)){
                type = CloneResults.Type.TypeII;
            }
        }

        return type;
    }

    private CloneResults<T> run(Class<T> type){
        Instant start = Instant.now();

        List<T> elements = project.getElements(type).asList();
        int size = elements.size();

        for(int i = 0; i < size; ++i){
            T t1 = elements.get(i);

            for(int j = i + 1; j < size; ++j){
                T t2 = elements.get(j);
                clones.update(t1, t2, getCloneType(t1, t2));
            }
        }

        System.gc();

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();

        logger.info(String.format("Computed clones for project %s in %d ms", this.project.getCommitId(), timeElapsed));

        return clones;
    }

    private static boolean isType1(Class<? extends Element> elementType, Difference difference) {
        return difference.isEmpty(getIgnore(elementType, CloneResults.Type.TypeI));
    }

    private static boolean isType2(Class<? extends Element> elementType, Difference difference) {
        return difference.isEmpty(getIgnore(elementType, CloneResults.Type.TypeII));
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

    private static Action.Type[] getIgnore(Class<? extends Element> elementType, CloneResults.Type cloneType) {
        switch (cloneType){
            case TypeI:
            {
                if(KeywordDefinition.class.isAssignableFrom(elementType)){
                    return new Action.Type[]{Action.Type.CHANGE_NAME,
                            Action.Type.CHANGE_DOCUMENTATION,
                            Action.Type.REMOVE_DOCUMENTATION,
                            Action.Type.ADD_DOCUMENTATION};
                }
                else if (Variable.class.isAssignableFrom(elementType)){
                    return new Action.Type[]{Action.Type.CHANGE_NAME};
                }
            }
            break;

            case TypeII:
            {
                if(KeywordDefinition.class.isAssignableFrom(elementType)){
                    return new Action.Type[]{Action.Type.CHANGE_NAME,
                            Action.Type.CHANGE_DOCUMENTATION,
                            Action.Type.REMOVE_DOCUMENTATION,
                            Action.Type.ADD_DOCUMENTATION,
                            Action.Type.CHANGE_STEP_ARGUMENTS,
                            Action.Type.CHANGE_STEP_RETURN_VALUES};
                }
                else if (Variable.class.isAssignableFrom(elementType)){
                    return new Action.Type[]{Action.Type.CHANGE_NAME};
                }
            }
            break;

            case TypeIII:
                break;
            case TypeIV:
                break;
        }

        return new Action.Type[]{};
    }
}
