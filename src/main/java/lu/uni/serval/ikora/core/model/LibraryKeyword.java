package lu.uni.serval.ikora.core.model;

import lu.uni.serval.ikora.core.analytics.visitor.NodeVisitor;
import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.builder.ValueResolver;
import lu.uni.serval.ikora.core.exception.RunnerException;
import lu.uni.serval.ikora.core.runner.Runtime;
import lu.uni.serval.ikora.core.types.BaseType;
import lu.uni.serval.ikora.core.types.BaseTypeList;

import java.util.*;

public abstract class LibraryKeyword implements Keyword {
    protected final Type type;
    protected final BaseTypeList argumentTypes;
    private final Set<SourceNode> dependencies;

    protected LibraryKeyword(Type type, BaseType... argumentTypes) {
        this.type = type;
        this.argumentTypes = new BaseTypeList(argumentTypes);
        this.dependencies = new HashSet<>();
    }

    protected LibraryKeyword(Type type){
        this.type = type;
        this.argumentTypes = new BaseTypeList();
        this.dependencies = new HashSet<>();
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
    public void execute(Runtime runtime) throws RunnerException {
        runtime.enterNode(this);

        run(runtime);

        runtime.exitNode(this);
    }

    protected abstract void run(Runtime runtime);

    public Documentation getDocumentation(){
        return new Documentation();
    }

    @Override
    public NodeList<Value> getReturnValues(){
        return new NodeList<>();
    }

    @Override
    public void addDependency(SourceNode node) {
        if(node == null){
            return;
        }

        this.dependencies.add(node);
    }

    @Override
    public void removeDependency(SourceNode node) {
        this.dependencies.remove(node);
    }

    @Override
    public Set<SourceNode> getDependencies() {
        return this.dependencies;
    }
}
