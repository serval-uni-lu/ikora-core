package tech.ikora.model;

import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.builder.ValueLinker;
import tech.ikora.runner.Runtime;
import tech.ikora.types.BaseType;

import java.util.*;

public abstract class LibraryKeyword implements Keyword {
    protected final Type type;
    protected final List<BaseType> argumentTypes;

    protected LibraryKeyword(Type type, BaseType... argumentTypes) {
        this.type = type;
        this.argumentTypes = Arrays.asList(argumentTypes);
    }

    protected LibraryKeyword(Type type){
        this.type = type;
        this.argumentTypes = Collections.emptyList();
    }

    public Type getType(){
        return this.type;
    }

    public List<BaseType> getArgumentTypes(){
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
        return ValueLinker.matches(getName(), name.getText());
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
    public List<Variable> getReturnVariables(){
        return Collections.emptyList();
    }

    @Override
    public void addDependency(Node node) {

    }

    @Override
    public Set<Node> getDependencies() {
        return null;
    }
}
