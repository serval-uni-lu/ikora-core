package tech.ikora.model;

import java.util.HashMap;
import java.util.Map;

public class Metadata {
    private final Map<String, Token> data;

    public Metadata(){
        this.data = new HashMap<>();
    }

    public void addEntry(String key, Token token){
        this.data.put(key, token);
    }
}
