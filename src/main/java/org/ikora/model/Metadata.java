package org.ikora.model;

import java.util.HashMap;
import java.util.Map;

public class Metadata {
    private final Map<String, Value> data;

    public Metadata(){
        this.data = new HashMap<>();
    }

    public void addEntry(String key, Value value){
        this.data.put(key, value);
    }
}
