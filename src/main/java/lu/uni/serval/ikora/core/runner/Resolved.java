package lu.uni.serval.ikora.core.runner;

import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.types.BaseType;
import lu.uni.serval.ikora.core.types.VariableType;

public class Resolved {
    private final String key;
    private final BaseType value;
    private final Argument origin;

    private Resolved(String key, BaseType value, Argument origin) {
        this.key = key;
        this.value = value;
        this.origin = origin;
    }

    public static Resolved createUnresolved(Argument origin){
        return new Resolved(null, new VariableType(origin.getName()), origin);
    }

    public static Resolved create(String key, BaseType value, Argument origin){
        return new Resolved(key, value, origin);
    }

    public static Resolved create(BaseType value, Argument origin){
        return new Resolved(null, value, origin);
    }

    public static Resolved create(BaseType value) {
        return new Resolved(null, value, null);
    }

    public boolean isResolved(){
        return value != null;
    }

    public boolean is(String name){
        if(key == null){
            return false;
        }

        if(key.isEmpty()){
            return false;
        }

        return key.equals(name);
    }

    public String getKey(){
        return key;
    }
    public BaseType getValue() {
        return value;
    }

    public Argument getOrigin() {
        return origin;
    }

    public boolean hasKey() {
        return key != null;
    }

    public boolean hasValue() {
        return value != null;
    }
}
