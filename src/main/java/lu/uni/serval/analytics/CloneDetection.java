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

    public static  <T extends  Element> Difference.Clone getCloneType(T t1, T t2){
        Difference.Clone clone = Difference.Clone.None;

        if(isSameSize(t1, t2) && !isTooShort(t1, t2)){
            Difference difference = Difference.of(t1, t2);

            if(difference.isCloneTypeI()){
                clone = Difference.Clone.TypeI;
            }
            else if(difference.isCloneTypeII()){
                clone = Difference.Clone.TypeII;
            }
        }

        return clone;
    }

    private CloneResults<T> run(Class<T> type){
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
}
