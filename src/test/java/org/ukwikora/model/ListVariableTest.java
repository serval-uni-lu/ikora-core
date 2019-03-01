package org.ukwikora.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class ListVariableTest {
    @Test
    public void checkNameMatching(){
        ListVariable test1 = new ListVariable("@{test1}");
        assertTrue(test1.matches("@{test1}"));
        assertTrue(test1.matches("@{TEST1}"));
        assertTrue(test1.matches("${test1}"));
        assertTrue(test1.matches("@{test1[0]}"));
        assertTrue(test1.matches("@{test1[10]}"));
        assertTrue(test1.matches("@{test1[5][3]}"));
        assertFalse(test1.matches("&{test1}"));
        assertFalse(test1.matches("@{test1[char]}"));
    }
}