package lu.uni.serval.ikora.core.types;

public class UnresolvedType extends BaseType {
    private UnresolvedType() {
        super("<UNRESOLVED TYPE>", "");
    }

    public static UnresolvedType get() {
        return new UnresolvedType();
    }
}
