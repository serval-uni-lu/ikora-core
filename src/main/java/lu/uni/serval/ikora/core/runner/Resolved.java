package lu.uni.serval.ikora.core.runner;

import lu.uni.serval.ikora.core.model.Argument;

public class Resolved {
    private final String key;
    private final String value;
    private final Argument origin;

    private Resolved(String key, String value, Argument origin) {
        this.key = key;
        this.value = value;
        this.origin = origin;
    }

    public static Resolved create(String value, Argument origin){
        return new Resolved("", value, origin);
    }

    public static Resolved create(String key, String value, Argument origin){
        return new Resolved(key, value, origin);
    }

    public static Resolved create(String value) {
        return new Resolved("", value, null);
    }

    public boolean is(String name){
        if(key.isEmpty()){
            return false;
        }

        return key.equals(name);
    }

    public String getKey(){
        return key;
    }
    public String getValue() {
        return value;
    }

    public Argument getOrigin() {
        return origin;
    }
}
