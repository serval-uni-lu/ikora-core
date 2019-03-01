package org.ukwikora.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class ScalarVariableTest {
    @Test
    public void checkNameMatching(){
        ScalarVariable test1 = new ScalarVariable("${test1}");
        assertTrue(test1.matches("${test1}"));
        assertTrue(test1.matches("${TEST1}"));
        assertFalse(test1.matches("&{test1}"));
        assertFalse(test1.matches("@{test1}"));
    }

}