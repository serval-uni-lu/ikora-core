package org.ukwikora.model;

import java.util.HashMap;
import java.util.Map;

public class Scope {
    ElementTable<Variable> global;
    Map<TestCase, ElementTable<Variable>> test;

    public Scope(){
        global = new ElementTable<>();
        test = new HashMap<>();
    }

    public Variable findTestVariable(TestCase testCase, String name) {
        if(!test.containsKey(testCase)){
            return null;
        }

        return test.get(testCase).findElement(name);
    }

    public Variable findGlobalVariable(String name) {
        return global.findElement(name);
    }
}
