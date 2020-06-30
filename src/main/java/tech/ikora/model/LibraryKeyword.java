package tech.ikora.model;

import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.builder.ValueResolver;
import tech.ikora.runner.Runtime;
import tech.ikora.types.BaseType;
import tech.ikora.types.BaseTypeList;

import java.util.*;

public abstract class LibraryKeyword implements Keyword {
    protected final Type type;
    protected final BaseTypeList argumentTypes;

    protected LibraryKeyword(Type type, BaseType... argumentTypes) {
        this.type = type;
        this.argumentTypes = new BaseTypeList(argumentTypes);
    }

    protected LibraryKeyword(Type type){
        this.type = type;
        this.argumentTypes = new BaseTypeList();
    }

    public Type getType(){
        return this.type;
    }

    public BaseTypeList getArgumentTypes(){
        return this.argumentTypes;
    }

    @Override
    public void accept(NodeVisitor visitor, VisitorMemory memory){
        visitor.visit(this, memory);
    }

    @Override
    public String getName(){
        return toKeyword(this.getClass());
    }

    public static String toKeyword(Class<? extends LibraryKeyword> libraryClass) {
        String name = libraryClass.getSimpleName();
        return name.replaceAll("([A-Z])", " $1").trim().toLowerCase();
    }

    public String getLibraryName(){
        String[] packages = this.getClass().getCanonicalName().split("\\.");

        for(int i = 0; i < packages.length; ++i){
            if(packages[i].equalsIgnoreCase("libraries") && i < packages.length - 1){
                return packages[i + 1];
            }
        }

        return "";
    }

    @Override
    public boolean matches(Token name) {
        return ValueResolver.matches(getName(), name.getText());
    }

    @Override
    public void execute(Runtime runtime) throws Exception{
        runtime.enterNode(this);

        run(runtime);

        runtime.exitNode(this);
    }

    protected abstract void run(Runtime runtime);

    public Tokens getDocumentation(){
        return new Tokens();
    }

    @Override
    public NodeList<Value> getReturnValues(){
        return new NodeList<>();
    }

    @Override
    public void addDependency(Node node) {

    }

    @Override
    public Set<Node> getDependencies() {
        return null;
    }
}
