package lu.uni.serval.ikora.core.runner;

import lu.uni.serval.ikora.core.model.Argument;
import lu.uni.serval.ikora.core.utils.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

public class Resolved {
    private final String key;
    private final String value;
    private final Argument origin;

    private Resolved(String key, String value, Argument origin) {
        this.key = key;
        this.value = value;
        this.origin = origin;
    }

    public static Resolved createUnresolved(Argument origin){
        return new Resolved(null, null, origin);
    }

    public static Resolved create(String value, Argument origin){
        final Pair<String,String> split = StringUtils.splitEqual(value);
        return new Resolved(split.getKey(), split.getValue(), origin);
    }

    public static Resolved create(String value) {
        return new Resolved("", value, null);
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
    public String getValue() {
        return value;
    }

    public Argument getOrigin() {
        return origin;
    }
}
