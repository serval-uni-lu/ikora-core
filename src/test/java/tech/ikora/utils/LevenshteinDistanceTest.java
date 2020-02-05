package tech.ikora.utils;

import tech.ikora.analytics.Action;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.ikora.Helpers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LevenshteinDistanceTest {

    @Test
    void testLevenshteinDistanceDifferentString() {
        String string1 = "First String";
        String string2 = "Second String";

        int distance = LevenshteinDistance.stringDistance(string1, string2);

        assertEquals(6, distance);
    }

    @Test
    void testLevenshteinIndexDifferentString() {
        String string1 = "First String";
        String string2 = "Second String";

        double index = LevenshteinDistance.stringIndex(string1, string2);

        Assertions.assertEquals(0.461538461538, index, Helpers.epsilon);
    }

    @Test
    void testRemoveStringFromList(){
        List<DifferentiableString> list1 = new ArrayList<>();
        list1.add(new DifferentiableString("Step 1"));
        list1.add(new DifferentiableString("Step 2"));
        list1.add(new DifferentiableString("Step 3"));

        List<DifferentiableString> list2 = new ArrayList<>();
        list2.add(new DifferentiableString("Step 2"));
        list2.add(new DifferentiableString("Step 3"));

        List<Action> differences = LevenshteinDistance.getDifferences(list1, list2);

        assertEquals(1, differences.size());
        assertEquals(Action.Type.REMOVE_STRING, differences.get(0).getType());
    }

    @Test
    void testAddStringToList(){
        List<DifferentiableString> list1 = new ArrayList<>();
        list1.add(new DifferentiableString("Step 2"));
        list1.add(new DifferentiableString("Step 3"));

        List<DifferentiableString> list2 = new ArrayList<>();
        list2.add(new DifferentiableString("Step 1"));
        list2.add(new DifferentiableString("Step 2"));
        list2.add(new DifferentiableString("Step 3"));

        List<Action> differences = LevenshteinDistance.getDifferences(list1, list2);

        assertEquals(1, differences.size());
        assertEquals(Action.Type.ADD_STRING, differences.get(0).getType());
    }
}
