package tech.ikora.types;

public class NumberType extends BaseType {
    public NumberType(String name){
        super(name, null);
    }

    public NumberType(String name, String defaultValue){
        super(name, defaultValue);
    }
}
