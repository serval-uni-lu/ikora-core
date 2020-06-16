package tech.ikora.types;

public class BooleanType extends BaseType {
    public BooleanType(String name){
        super(name, null);
    }

    public BooleanType(String name, String defaultValue){
        super(name, defaultValue);
    }
}
