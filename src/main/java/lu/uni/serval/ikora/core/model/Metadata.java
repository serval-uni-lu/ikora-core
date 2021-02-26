package lu.uni.serval.ikora.core.model;

import java.util.HashMap;
import java.util.Map;

public class Metadata {
    private final Map<Token, Token> data;

    public Metadata(){
        this.data = new HashMap<>();
    }

    public void addEntry(Token key, Token token){
        this.data.put(key, token);
    }

    public Token get(String name) {
        final Token key= data.keySet().stream()
                .filter(token -> token.getText().equalsIgnoreCase(name))
                .findAny()
                .orElse(null);

        return data.get(key);
    }

    public int size() {
        return data.size();
    }
}
