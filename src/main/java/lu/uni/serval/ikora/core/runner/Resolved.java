package lu.uni.serval.ikora.core.runner;

import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.parser.VariableParser;
import lu.uni.serval.ikora.core.runner.exception.InternalException;
import lu.uni.serval.ikora.core.utils.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;

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

    public static Resolved create(String key, String value, Argument origin){
        return new Resolved(key, value, origin);
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

    public boolean hasKey() {
        return key != null;
    }

    public boolean hasValue() {
        return value != null;
    }

    public Value toValue() throws InternalException {
        if(hasKey()){
            final Literal entryKey = new Literal(Token.fromString(getKey()));
            final Literal entryValue = new Literal(Token.fromString(getValue()));

            return new DictionaryEntry(entryKey, entryValue);
        }

        if(hasValue()){
            return new Literal(Token.fromString(getValue()));
        }

        final Optional<Variable> variable = VariableParser.parse(Token.fromString(getOrigin().getName()));

        if(variable.isPresent()){
            return variable.get();
        }

        throw new InternalException("Invalid resolution: " + getOrigin().getName() + " should be a variable name.");
    }
}
