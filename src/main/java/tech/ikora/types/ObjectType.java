package tech.ikora.types;

public class ObjectType extends BaseType {
    public ObjectType(String name) {
        super(name, null);
    }

    public ObjectType(String name, String defaultValue) {
        super(name, defaultValue);
    }
}
