package tech.ikora.types;

public class NamedType extends BaseType {
    public NamedType(String name) {
        super(name, null);
    }

    public NamedType(String name, String defaultValue) {
        super(name, defaultValue);
    }
}
